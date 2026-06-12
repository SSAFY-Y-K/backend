package com.ssafy.passit.auth.controller;

import com.ssafy.passit.auth.dto.LoginRequest;
import com.ssafy.passit.auth.dto.LoginResponse;
import com.ssafy.passit.auth.dto.LoginResult;
import com.ssafy.passit.auth.dto.RefreshResponse;
import com.ssafy.passit.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "인증/인가 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = """
        로그인 성공 시 access token 및 refresh token 발급
        access token은 body에, refresh token인 cookie에 실어 리턴
    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        LoginResult result = authService.login(request);

        LoginResponse response = new LoginResponse(result.getAccessToken());
        var refreshTokenCookie = ResponseCookie.from(
                "refreshToken",
                result.getRefreshToken()
        )
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 성공 시 refresh token 무효화")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        authService.logout(refreshToken);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "토큰 재발급", description = "refresh token 기반으로 access token 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "access token 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "refresh token 유효하지 않아 재발급 거부")
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        return ResponseEntity.ok().body(authService.refresh(refreshToken));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialException(
            BadCredentialsException exception
    ) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle(exception.getMessage());
        problemDetail.setDetail("Refresh Token is Invalid");

        return problemDetail;
    }
}
