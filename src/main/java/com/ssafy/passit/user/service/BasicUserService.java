package com.ssafy.passit.user.service;

import com.ssafy.passit.user.dto.SignupRequest;
import com.ssafy.passit.user.exception.ValidationException;
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
    public void login() {

    }

    /**
     * 비밀번호와 비밀번호 확인이 일치하는지 확인
     * @param password 비밀번호
     * @param confirmPassword 비밀번호 확인
     * @throws ValidationException 비밀번호가 일치하지 않음
     */
    private void validatePassword(String password, String confirmPassword) {
        boolean isMatched = password.equals(confirmPassword);
        if (!isMatched) {
            throw new ValidationException("비밀번호가 일치하지 않습니다.", "confirmPassword");
        }
    }

    /**
     * 유저네임 중복 체크
     * @param username 확인할 유저네임
     * @throws ValidationException 이미 존재하는 유저네임
     */
    private void validateUsernameUnique(String username) {
        int count = userRepository.countByUsername(username);
        if (count > 0) {
            throw new ValidationException("이미 존재하는 유저네임입니다.", "username");
        }
    }

    /**
     * 닉네임 중복 체크
     * @param nickname 확인할 닉네임
     * @throws ValidationException 이미 존재하는 닉네임
     */
    private void validateNicknameUnique(String nickname) {
        int count = userRepository.countByNickname(nickname);
        if (count > 0) {
            throw new ValidationException("이미 존재하는 닉네임입니다.", "nickname");
        }
    }
}
