package com.ssafy.passit.user.service;

import com.ssafy.passit.user.dto.SignupRequest;

public interface UserService {

    /**
     * 회원 가입 메서드
     */
    void signup(SignupRequest signupRequest);

    void login();
}
