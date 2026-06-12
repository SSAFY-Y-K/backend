package com.ssafy.passit.auth.service;

import com.ssafy.passit.auth.dto.LoginRequest;
import com.ssafy.passit.auth.dto.LoginResult;
import com.ssafy.passit.auth.dto.RefreshResponse;

public interface AuthService {

    /**
     * 로그인
     * @param request 사용자가 입력한 유저네임과 패스워드
     * @return 로그인 성공 시 access token과 refresh token 리턴
     */
    LoginResult login(LoginRequest request);

    /**
     * 로그아웃
     * @param refreshToken 로그아웃할 사용자의 refresh token
     */
    void logout(String refreshToken);

    /**
     * refresh token으로 새로운 access token 발급
     * @param refreshToken
     * @return 새로 발급한 access token
     */
    RefreshResponse refresh(String refreshToken);
}
