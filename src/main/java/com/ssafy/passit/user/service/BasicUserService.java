package com.ssafy.passit.user.service;

import com.ssafy.passit.user.dto.*;
import com.ssafy.passit.user.exception.SignupValidationException;
import com.ssafy.passit.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(SignupRequest signupRequest) {
        validatePassword(signupRequest.getPassword(), signupRequest.getConfirmPassword());
        validateUsernameUnique(signupRequest.getUsername());
        validateNicknameUnique(signupRequest.getNickname());

        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userMapper.insert(signupRequest);
    }

    /**
     * 비밀번호와 비밀번호 확인이 일치하는지 확인
     * @param password 비밀번호
     * @param confirmPassword 비밀번호 확인
     * @throws SignupValidationException 비밀번호가 일치하지 않음
     */
    private void validatePassword(String password, String confirmPassword) {
        boolean isMatched = password.equals(confirmPassword);
        if (!isMatched) {
            throw new SignupValidationException("비밀번호가 일치하지 않습니다.", "confirmPassword");
        }
    }

    /**
     * 유저네임 중복 체크
     * @param username 확인할 유저네임
     * @throws SignupValidationException 이미 존재하는 유저네임
     */
    private void validateUsernameUnique(String username) {
        int count = userMapper.countByUsername(username);
        if (count > 0) {
            throw new SignupValidationException("이미 존재하는 유저네임입니다.", "username");
        }
    }

    /**
     * 닉네임 중복 체크
     * @param nickname 확인할 닉네임
     * @throws SignupValidationException 이미 존재하는 닉네임
     */
    private void validateNicknameUnique(String nickname) {
        int count = userMapper.countByNickname(nickname);
        if (count > 0) {
            throw new SignupValidationException("이미 존재하는 닉네임입니다.", "nickname");
        }
    }
}
