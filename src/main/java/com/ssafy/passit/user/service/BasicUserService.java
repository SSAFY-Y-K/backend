package com.ssafy.passit.user.service;

import com.ssafy.passit.user.dto.*;
import com.ssafy.passit.user.exception.LoginFailureException;
import com.ssafy.passit.user.exception.SignupValidationException;
import com.ssafy.passit.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signup(SignupRequest signupRequest) {
        validatePassword(signupRequest.getPassword(), signupRequest.getConfirmPassword());
        validateUsernameUnique(signupRequest.getUsername());
        validateNicknameUnique(signupRequest.getNickname());

        userRepository.insert(signupRequest);
    }

    @Override
    public SessionUser login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new LoginFailureException("아이디 또는 비밀번호가 다릅니다."));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new LoginFailureException("아이디 또는 비밀번호가 다릅니다.");
        }

        return SessionUser.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
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
        int count = userRepository.countByUsername(username);
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
        int count = userRepository.countByNickname(nickname);
        if (count > 0) {
            throw new SignupValidationException("이미 존재하는 닉네임입니다.", "nickname");
        }
    }
}
