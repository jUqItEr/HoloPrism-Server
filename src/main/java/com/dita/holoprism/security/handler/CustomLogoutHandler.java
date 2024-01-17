package com.dita.holoprism.security.handler;

import com.dita.holoprism.security.auth.PrincipalDetails;
import com.dita.holoprism.security.jwt.JwtTokenProvider;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler{

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserEntity user = principalDetails.getUser();
        userRepository.updateToken(user.getId(), null, null);
        SecurityContextHolder.clearContext();
    }
}
