package com.dita.holoprism.security.config;

import com.dita.holoprism.security.handler.CustomLogoutHandler;
import com.dita.holoprism.security.handler.OAuth2SuccessHandler;
import com.dita.holoprism.security.jwt.*;
import com.dita.holoprism.security.service.OAuth2UserService;
import com.dita.holoprism.user.repository.UserRepository;
import com.dita.holoprism.user.service.UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final OAuth2UserService service;
    private final CorsConfig corsConfig;
    private final JwtTokenProvider jwtTokenProvider;
    private final CorsFilter corsFilter;
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final UserRepository userRepository;
    private final CustomLogoutHandler customLogoutHandler;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)

            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
            .authorizeHttpRequests(request -> request
                .dispatcherTypeMatchers(DispatcherType.REQUEST)
                    .permitAll()
                .anyRequest()
                    .permitAll()
            )
            .formLogin(login -> login
                    .defaultSuccessUrl("/index", true)
                    .successHandler(oAuth2SuccessHandler)
                    .permitAll()
            )
            .oauth2Login(session -> session
                .userInfoEndpoint(info -> info
                    .userService(service)
                )
                    .successHandler(oAuth2SuccessHandler)
            )
                .logout(logout -> logout
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessUrl("/login"));

        return http.build();
    }
}
