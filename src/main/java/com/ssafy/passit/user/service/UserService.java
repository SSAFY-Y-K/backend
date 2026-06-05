package com.ssafy.passit.user.service;

import com.ssafy.passit.user.dto.LoginRequest;
import com.ssafy.passit.user.dto.SessionUser;
import com.ssafy.passit.user.dto.SignupRequest;
import com.ssafy.passit.user.dto.User;

public interface UserService {

    /**
     * 회원 가입
     */
    void signup(SignupRequest signupRequest);

    /**
     * 로그인
     */
    SessionUser login(LoginRequest loginRequest);
}
