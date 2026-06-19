package com.ssafy.passit.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.authorizeHttpRequests(
                registry -> registry
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/problems/algorithm/**").hasAuthority("ADMIN")
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

        httpSecurity.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        httpSecurity.exceptionHandling(
                (exception) -> {
                    exception.authenticationEntryPoint(
                            jwtAuthenticationEntryPoint
                    );
                }
        );


        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) {
        return configuration.getAuthenticationManager();
    }
}
