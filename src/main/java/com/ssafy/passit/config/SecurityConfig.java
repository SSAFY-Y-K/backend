package com.ssafy.passit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.authorizeHttpRequests(
                registry -> registry
                        .anyRequest().permitAll()
        );

        httpSecurity.formLogin(
                AbstractHttpConfigurer::disable
        );
        httpSecurity.httpBasic(
                AbstractHttpConfigurer::disable
        );
        httpSecurity.csrf(
                AbstractHttpConfigurer::disable
        );
        httpSecurity.cors(
                AbstractHttpConfigurer::disable
        );

        return httpSecurity.build();
    }
}
