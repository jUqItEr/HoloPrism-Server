package com.dita.holoprism.security.service;

import com.dita.holoprism.security.service.provider.GoogleUserInfo;
import com.dita.holoprism.security.service.provider.KakaoUserInfo;
import com.dita.holoprism.security.service.provider.NaverUserInfo;
import com.dita.holoprism.security.service.provider.OAuth2UserInfo;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = LocalDateTime.now().format(formatter);
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest : " + userRequest); //TODO
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue()); //TODO
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); //TODO
        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());//TODO

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }else {
            System.out.println("what?"); //TODO
        }

        String userId = oAuth2UserInfo.getSocialType() + "_" + oAuth2UserInfo.getSocialId();
        Optional<UserEntity> userOptional = userRepository.findByIdAndProvider(userId, oAuth2UserInfo.getSocialType());

        UserEntity user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // 여기에서 방문시각 업데이트 로그인 한거니까
        } else {
            // 회원가입

            user = UserEntity.builder()
                    .id(userId)
                    .email(oAuth2UserInfo.getEmail())
                    .nickname(oAuth2UserInfo.getName())
                    .provider(oAuth2UserInfo.getSocialType())
                    .image(oAuth2UserInfo.getImage())
                    .permission(0)
                    .createdTime(String.valueOf(formattedDateTime))
                    .visitedTime(String.valueOf(formattedDateTime))
                    .build();

            userRepository.save(user); // 최초 회원가입 createdTime now
        }
        return UserEntity.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .password(null)
                .email(user.getEmail())
                .image(user.getImage())
                .createdTime(user.getCreatedTime())
                .visitedTime(user.getVisitedTime())
                .permission(user.getPermission())
                .attributes(oAuth2User.getAttributes())
                .layout(user.getLayout())
                .build();
    }
}
