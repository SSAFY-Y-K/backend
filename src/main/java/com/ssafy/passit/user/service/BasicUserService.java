package com.ssafy.passit.user.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.post.mapper.PostMapper;
import com.ssafy.passit.post.model.Post;
import com.ssafy.passit.submission.dto.MySubmissionResponse;
import com.ssafy.passit.submission.mapper.SubmissionMapper;
import com.ssafy.passit.user.dto.*;
import com.ssafy.passit.user.exception.SignupValidationException;
import com.ssafy.passit.user.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final SubmissionMapper submissionMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileResponse getProfile(Long userId) {
        User user = userMapper.findUserById(userId);
        if (user == null) throw new ApiException(ErrorCode.INTERNAL_ERROR, "사용자를 찾을 수 없습니다.");
        return UserProfileResponse.from(user);
    }

    @Override
    public List<Post> getMyPosts(Long userId) {
        return postMapper.findByUserId(userId);
    }

    @Override
    public List<MySubmissionResponse> getMySubmissions(Long userId) {
        return submissionMapper.findByUserId(userId);
    }

    @Override
    public void signup(SignupRequest request) {
        log.info("Signup requested. username={}, nickname={}", request.getUsername(), request.getNickname());
        validatePassword(request.getPassword(), request.getConfirmPassword());
        validateUsernameUnique(request.getUsername());
        validateNicknameUnique(request.getNickname());

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userMapper.insert(request);
        log.info("Signup completed. username={}, nickname={}", request.getUsername(), request.getNickname());
    }

    /**
     * 비밀번호와 비밀번호 확인의 유효성 검증
     * @param password 비밀번호
     * @param confirmPassword 비밀번호 확인
     * @throws SignupValidationException
     */
    private void validatePassword(String password, String confirmPassword) {
        if (password == null || password.strip().isBlank()) {
            log.warn("Signup validation failed. field=password, reason=blank");
            throw new SignupValidationException("비밀번호가 비어 있습니다.", "password");
        } else if (confirmPassword == null || confirmPassword.strip().isBlank()) {
            log.warn("Signup validation failed. field=confirmPassword, reason=blank");
            throw new SignupValidationException("비밀번호 확인이 비어 있습니다.", "confirmPassword");
        }
        boolean isMatched = password.equals(confirmPassword);
        if (!isMatched) {
            log.warn("Signup validation failed. field=confirmPassword, reason=mismatch");
            throw new SignupValidationException("비밀번호가 일치하지 않습니다.", "confirmPassword");
        }
    }

    /**
     * 유저네임 중복 체크
     * @param username 확인할 유저네임
     * @throws SignupValidationException 이미 존재하는 유저네임
     */
    private void validateUsernameUnique(String username) {
        if (username == null || username.strip().isBlank()) {
            log.warn("Signup validation failed. field=username, reason=blank");
            throw new SignupValidationException("아이디가 비어있습니다.", "username");
        }

        int count = userMapper.countByUsername(username);
        if (count > 0) {
            log.warn("Signup validation failed. field=username, reason=duplicated, username={}", username);
            throw new SignupValidationException("이미 존재하는 아이디입니다.", "username");
        }
    }

    /**
     * 닉네임 중복 체크
     * @param nickname 확인할 닉네임
     * @throws SignupValidationException 이미 존재하는 닉네임
     */
    private void validateNicknameUnique(String nickname) {
        if (nickname == null || nickname.strip().isBlank()) {
            log.warn("Signup validation failed. field=nickname, reason=blank");
            throw new SignupValidationException("닉네임이 비어 있습니다.", "nickname");
        }

        int count = userMapper.countByNickname(nickname);
        if (count > 0) {
            log.warn("Signup validation failed. field=nickname, reason=duplicated, nickname={}", nickname);
            throw new SignupValidationException("이미 존재하는 닉네임입니다.", "nickname");
        }
    }
}
