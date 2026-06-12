package com.ssafy.passit.user.service;

import com.ssafy.passit.user.dto.SignupRequest;

public interface UserService {

    /**
     * 회원 가입
     */
    void signup(SignupRequest signupRequest);

}
