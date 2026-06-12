package com.ssafy.passit.auth.service;

import com.ssafy.passit.auth.JwtProvider;
import com.ssafy.passit.auth.dto.LoginRequest;
import com.ssafy.passit.auth.dto.LoginResult;
import com.ssafy.passit.auth.dto.RefreshResponse;
import com.ssafy.passit.auth.dto.RefreshTokenResult;
import com.ssafy.passit.security.CustomUserDetails;
import com.ssafy.passit.user.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public LoginResult login(
            LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtProvider.createAccessToken(principal.getUserId(), principal.getUsername(), principal.getRole());

        String refreshToken = jwtProvider.createRefreshToken(principal.getUserId(), principal.getUsername());

        userMapper.updateRefreshToken(principal.getUserId(), refreshToken);

        return LoginResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        Claims claims = jwtProvider.parseToken(refreshToken);
        Long userId = claims.get("userId", Long.class);

        userMapper.clearRefreshToken(userId);
    }

    @Override
    public RefreshResponse refresh(String refreshToken) {
        var claims = jwtProvider.parseToken(refreshToken);

        Long userId = claims.get("userId", Long.class);

        RefreshTokenResult result = userMapper.findByUserId(userId);

        if (result == null || !refreshToken.equals(result.getRefreshToken())) {
            throw new BadCredentialsException("Invalid Refresh Token");
        }

        String newAccessToken = jwtProvider.createAccessToken(
                result.getUserId(),
                result.getUsername(),
                result.getRole()
        );

        return new RefreshResponse(newAccessToken);
    }
}
