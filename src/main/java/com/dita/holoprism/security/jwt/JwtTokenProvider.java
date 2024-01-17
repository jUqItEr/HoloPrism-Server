package com.dita.holoprism.security.jwt;

import com.dita.holoprism.security.auth.PrincipalDetails;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements InitializingBean {

   private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
   private static final String AUTHORITIES_KEY = "role";
   private final String secret;
   private final long tokenValidityInMilliseconds;
   private final long tokenExpirationInSeconds;
   private Key key;
   private String accessHeader = "Authorization";
   private final UserRepository userRepository;

   public JwtTokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
      @Value("${jwt.token-expiration-in-seconds}") long tokenExpirationInSeconds,
      UserRepository userRepository) {
      this.secret = secret;
      this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
      this.tokenExpirationInSeconds = tokenExpirationInSeconds * 1000;
      this.userRepository = userRepository;
   }

   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }


   public String createToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));

      long now = (new Date()).getTime();
      Date validity = new Date(now + this.tokenValidityInMilliseconds);
      System.out.println("token generated"); // TODO
      return Jwts.builder()
         .setSubject(authentication.getName())
         .claim(AUTHORITIES_KEY, authorities)
         .signWith(key, SignatureAlgorithm.HS512)
         .setExpiration(validity)
         .compact();
   }

   public String createRefreshToken() {

      long now = (new Date()).getTime();
      Date validity = new Date(now + this.tokenExpirationInSeconds);
      System.out.println(" refresh 토큰 생성됨"); // TODO
      return Jwts.builder()
              .setSubject("refreshToken")
              .signWith(key, SignatureAlgorithm.HS512)
              .setExpiration(validity)
              .compact();
   }

   public Authentication getAuthentication(String token) {
      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      System.out.println("claims : " + claims); // TODO
      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      //User principal = new User(claims.getSubject(), "", authorities);

      UserEntity user;

      if (claims.getSubject().startsWith("google")) {
         user = getPrincipal(claims.getSubject(), "google");
      } else if (claims.getSubject().startsWith("kakao")) {
         user = getPrincipal(claims.getSubject(), "kakao");
      } else if (claims.getSubject().startsWith("naver")) {
         user = getPrincipal(claims.getSubject(), "naver");
      } else {
         user = getPrincipal(claims.getSubject(), null);
      }

      PrincipalDetails principalDetails = new PrincipalDetails(user);

      System.out.println("princialDetails : " + principalDetails); // TODO
      System.out.println("token : " + token); // TODO
      System.out.println("authorities : " + authorities); // TODO
      return new UsernamePasswordAuthenticationToken(principalDetails, token, authorities);
   }

   public UserEntity getPrincipal(String userId, String social) {
      Optional<UserEntity> userOptional = userRepository.findByIdAndProvider(userId, social);
      UserEntity user = userOptional.get();

      return user;
   }

   public String getSubject(String token) {
      try {
         Claims claims = Jwts
                 .parserBuilder()
                 .setSigningKey(key)
                 .build()
                 .parseClaimsJws(token)
                 .getBody();

         return claims.getSubject();
      } catch (ExpiredJwtException e) {
         return e.getClaims().getSubject();
      }
   }

   public String validateToken(String token) {
      String isValidate = "";
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         isValidate = "ACCESS";
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         logger.info("잘못된 JWT 서명입니다.");
         isValidate = "DENIED";
      } catch (ExpiredJwtException e) {
         logger.info("만료된 JWT 토큰입니다.");
         isValidate = "EXPIRED";
      } catch (UnsupportedJwtException e) {
         logger.info("지원되지 않는 JWT 토큰입니다.");
         isValidate = "DENIED";
      } catch (IllegalArgumentException e) {
         logger.info("JWT 토큰이 잘못되었습니다.");
         isValidate = "DENIED";
      }
      return isValidate;
   }

   public Optional<String> extractAccessToken(HttpServletRequest request) {
      return Optional.ofNullable(request.getHeader(accessHeader))
              .filter(refreshToken -> refreshToken.startsWith("Bearer "))
              .map(refreshToken -> refreshToken.replace("Bearer ", ""));
   }
}