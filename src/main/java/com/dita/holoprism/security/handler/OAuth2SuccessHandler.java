package com.dita.holoprism.security.handler;

import com.dita.holoprism.security.auth.PrincipalDetails;
import com.dita.holoprism.security.jwt.JwtTokenProvider;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserEntity user = principalDetails.getUser();
        System.out.println(user); //TODO

        userRepository.updateVisitedTime(user.getId());

        String jwtAccessToken = jwtTokenProvider.createToken(authentication);
        String jwtRefreshToken = jwtTokenProvider.createRefreshToken();

        System.out.println(jwtAccessToken);//TODO
        System.out.println(jwtRefreshToken);//TODO
        if (userRepository.findUserRefreshToken(user.getId()) == 1) { // refreshToken이 이미 존재할 경우

            String preRefreshToken = userRepository.getUserRefreshToken(user.getId());
            if (jwtTokenProvider.validateToken(preRefreshToken)) { // 토큰 유효성 검사
                userRepository.updateToken(user.getId(), jwtAccessToken, preRefreshToken); // AccessToken만 재발급
            } else {
                userRepository.updateToken(user.getId(), jwtAccessToken, jwtRefreshToken); // AccessToken 및 RefreshToken 둘다 재발급
            }
        } else { // refreshToken이 존재하지 않으면 새로 발급한 토큰을 그대로 저장
            userRepository.updateToken(user.getId(), jwtAccessToken, jwtRefreshToken);
        }

        String targetUrl = "";

        response.addHeader("Authorization", "Bearer " + jwtAccessToken);
    }
}