package com.ssafy.passit.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        String exception = (String) request.getAttribute("jwt_exception");

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        // 토큰 만료로 인증 실패
        if ("TOKEN_EXPIRED".equals(exception)) {
            problemDetail.setTitle("Token Expired");
            problemDetail.setDetail("Access token has expired.");
        // 유효하지 않은 토큰으로 인증 실패
        } else if ("INVALID_TOKEN".equals(exception)) {
            problemDetail.setTitle("Invalid Token");
            problemDetail.setDetail("Token is invalid.");
        // 기타 인증 실패
        } else {
            problemDetail.setTitle("Unauthorized");
            problemDetail.setDetail("Authentication is required.");
        }

        response.setStatus(
                HttpStatus.UNAUTHORIZED.value()
        );

        response.setContentType(
                MediaType.APPLICATION_PROBLEM_JSON_VALUE
        );

        objectMapper.writeValue(response.getWriter(), problemDetail);
    }
}
