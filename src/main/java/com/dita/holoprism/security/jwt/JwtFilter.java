package com.dita.holoprism.security.jwt;

import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

   private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
   public static final String AUTHORIZATION_HEADER = "Authorization";
   private final JwtTokenProvider jwtTokenProvider;
   private final UserRepository userRepository;
   private final AuthenticationManagerBuilder authenticationManagerBuilder;

   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
      String jwt = resolveToken(httpServletRequest, AUTHORIZATION_HEADER);
      String requestURI = httpServletRequest.getRequestURI();

      if ("/logout".equals(httpServletRequest.getRequestURI())) {
         return;
      }

      if (StringUtils.hasText(jwt) && (jwtTokenProvider.validateToken(jwt).equals("ACCESS"))) {
         Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
         SecurityContextHolder.getContext().setAuthentication(authentication);

         logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);

      } else if (StringUtils.hasText(jwt) && (jwtTokenProvider.validateToken(jwt).equals("EXPIRED"))){
         logger.debug("Access token expired, uri: {}", requestURI);
         String refreshToken = null;

         String userId = jwtTokenProvider.getSubject(jwt);
         refreshToken = userRepository.getUserRefreshToken(userId); // userId로 refreshToken 조회
            // DB에 저장된 refresh token 검증
            if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateToken(refreshToken).equals("ACCESS")) {
               // access token 재발급
               UserEntity user = userRepository.findById(userId).orElse(null);
               String userPassword = user.getPassword();
               user.setPassword(null);
               Authentication authentication =
                       new UsernamePasswordAuthenticationToken(user, userPassword, user.getAuthorities());

               String newAccessToken = jwtTokenProvider.createToken(authentication);
               SecurityContextHolder.getContext().setAuthentication(authentication);
               userRepository.updateAccessToken(userId, newAccessToken);

               ((HttpServletResponse) servletResponse).setHeader(AUTHORIZATION_HEADER, "Bearer " + newAccessToken);
               logger.info("Reissue access token");

            } else {
               userRepository.updateToken(userId, "", "");
               new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse, SecurityContextHolder.getContext().getAuthentication());

               return;
            }
         // refresh token이 없거나 잘못된 경우 바로 logout
      }

      filterChain.doFilter(servletRequest, servletResponse);
   }

   private String resolveToken(HttpServletRequest request, String header) {
      String bearerToken = request.getHeader(header);

      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }
      return null;
   }
}
