package com.ssafy.passit.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        // 관리자 전용
                        .requestMatchers(HttpMethod.DELETE, "/api/problems/algorithm/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        // 로그인 필요
                        .requestMatchers(HttpMethod.POST, "/api/posts").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/posts/*/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/*/comments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/problems/algorithm/generate").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/problems/*/submissions").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/problems/*/run").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/problems/*/reports").authenticated()
                        .requestMatchers("/api/users/me/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me").authenticated()
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
