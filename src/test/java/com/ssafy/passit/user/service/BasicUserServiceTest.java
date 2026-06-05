package com.ssafy.passit.user.service;

import com.ssafy.passit.user.dto.SignupRequest;
import com.ssafy.passit.user.exception.ValidationException;
import com.ssafy.passit.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String NICKNAME = "nickname";

    @Mock
    UserRepository repository;

    @InjectMocks
    BasicUserService userService;

    @Test
    @DisplayName("회원 가입 성공")
    void signup_success() {
        SignupRequest request = signupRequest();

        userService.signup(request);

        verify(repository).insert(any());
    }

    @Test
    @DisplayName("비밀번호 불일치로 회원가입 실패")
    void signup_failure_mismatchPassword() {
        SignupRequest request = passwordMismatchRequest();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.signup(request));

        assertEquals("confirmPassword", exception.getErrorField());
        verify(repository, never()).insert(any());
    }

    @Test
    @DisplayName("유저네임 중복으로 회원가입 실패")
    void signup_failure_duplicateUsername() {

        SignupRequest request = signupRequest();

        given(repository.countByUsername(request.getUsername())).willReturn(1);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.signup(request));

        assertEquals("username", exception.getErrorField());
        verify(repository, never()).insert(any());

    }

    @Test
    @DisplayName("닉네임 중복으로 회원가입 실패")
    void signup_failure_duplicateNickname() {

        SignupRequest request = signupRequest();

        given(repository.countByNickname(request.getNickname())).willReturn(1);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.signup(request));

        assertEquals("nickname", exception.getErrorField());
        verify(repository, never()).insert(any());
    }

    private SignupRequest passwordMismatchRequest() {
        return SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .confirmPassword("mismatch")
                .nickname(NICKNAME)
                .build();
    }

    private SignupRequest signupRequest() {
        return SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .confirmPassword(PASSWORD)
                .nickname(NICKNAME)
                .build();
    }
}