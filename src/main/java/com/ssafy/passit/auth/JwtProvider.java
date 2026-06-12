package com.ssafy.passit.auth;

import com.ssafy.passit.common.type.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {

    private final Duration ACCESS_TOKEN_EXPIRE = Duration.ofHours(1L);
    private final Duration REFRESH_TOKEN_EXPIRE = Duration.ofDays(30L);
    private final SecretKey key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * access token 생성하여 발급
     * @param userId
     * @param username
     * @param role
     * @return
     */
    public String createAccessToken(Long userId, String username, Role role) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("role", role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ACCESS_TOKEN_EXPIRE)))
                .signWith(key)
                .compact();
    }

    /**
     * refresh token 생성하여 발급
     * @param userId
     * @param username
     * @return
     */
    public String createRefreshToken(Long userId, String username) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(REFRESH_TOKEN_EXPIRE)))
                .signWith(key)
                .compact();
    }

    /**
     * 토큰 파싱
     * @param token
     * @return
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
