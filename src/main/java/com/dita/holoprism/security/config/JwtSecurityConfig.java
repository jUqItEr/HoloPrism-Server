package com.dita.holoprism.security.config;

import com.dita.holoprism.security.jwt.JwtFilter;
import com.dita.holoprism.security.jwt.JwtTokenProvider;
import com.dita.holoprism.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(
            new JwtFilter(jwtTokenProvider, userRepository),
            UsernamePasswordAuthenticationFilter.class
        );
    }
}
