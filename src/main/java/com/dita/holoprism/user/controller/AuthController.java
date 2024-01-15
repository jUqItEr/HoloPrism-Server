package com.dita.holoprism.user.controller;

import com.dita.holoprism.security.auth.PrincipalDetails;
import com.dita.holoprism.security.jwt.JwtFilter;
import com.dita.holoprism.security.jwt.JwtTokenProvider;
import com.dita.holoprism.user.dto.LoginDto;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public AuthController(JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/token/principal")
    public ResponseEntity<?> getPrincipal() {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity user = principalDetails.getUser();
        UserEntity userResponse = UserEntity.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .image(user.getImage())
                .createdTime(user.getCreatedTime())
                .visitedTime(user.getVisitedTime())
                .accessToken(user.getAccessToken())
                .permission(user.getPermission())
                .build();

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/token/logout")
    public ResponseEntity<?> logout(@RequestHeader(AUTHORIZATION_HEADER) String accessToken) {

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        userRepository.updateToken(principalDetails.getUser().getId(), "", ""); // DB에 저장된 토큰을 모두 삭제함

        return ResponseEntity.ok("로그아웃 성공");
    }
}
