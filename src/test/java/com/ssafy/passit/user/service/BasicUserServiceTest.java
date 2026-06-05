package com.ssafy.passit.user.service;

import com.ssafy.passit.user.dto.*;
import com.ssafy.passit.user.exception.LoginFailureException;
import com.ssafy.passit.user.exception.SignupValidationException;
import com.ssafy.passit.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    private static final Long USERID = 1L;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String NICKNAME = "nickname";
    private static final Role ROLE = Role.USER;

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

        SignupValidationException exception = assertThrows(SignupValidationException.class,
                () -> userService.signup(request));

        assertEquals("confirmPassword", exception.getErrorField());
        verify(repository, never()).insert(any());
    }

    @Test
    @DisplayName("유저네임 중복으로 회원가입 실패")
    void signup_failure_duplicateUsername() {

        SignupRequest request = signupRequest();

        given(repository.countByUsername(request.getUsername())).willReturn(1);

        SignupValidationException exception = assertThrows(SignupValidationException.class,
                () -> userService.signup(request));

        assertEquals("username", exception.getErrorField());
        verify(repository, never()).insert(any());

    }

    @Test
    @DisplayName("닉네임 중복으로 회원가입 실패")
    void signup_failure_duplicateNickname() {

        SignupRequest request = signupRequest();

        given(repository.countByNickname(request.getNickname())).willReturn(1);

        SignupValidationException exception = assertThrows(SignupValidationException.class,
                () -> userService.signup(request));

        assertEquals("nickname", exception.getErrorField());
        verify(repository, never()).insert(any());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        LoginRequest request = loginRequest();
        User user = user();
        SessionUser sessionUser = sessionUser();

        given(repository.findByUsername(request.getUsername())).willReturn(Optional.of(user));

        SessionUser resultSessionUser = userService.login(request);

        assertEquals(sessionUser, resultSessionUser);
        verify(repository).findByUsername(any());
    }

    @Test
    @DisplayName("일치하는 유저네임 없어 로그인 실패")
    void login_failure_nonexistUsername() {

        LoginRequest request = loginRequest();

        given(repository.findByUsername(request.getUsername())).willReturn(Optional.empty());

        assertThrows(LoginFailureException.class,
                () -> userService.login(request));

        verify(repository).findByUsername(any());
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않아 로그인 실패")
    void login_failure_mismatchPassword() {

        LoginRequest request = loginRequest();
        User wrongUser = wrongUser();

        given(repository.findByUsername(request.getUsername())).willReturn(Optional.of(wrongUser));

        assertThrows(LoginFailureException.class,
                () -> userService.login(request));

        verify(repository).findByUsername(any());
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

    private LoginRequest loginRequest() {
        return LoginRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

    }

    private User user() {
        return User.builder()
                .userId(USERID)
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .role(ROLE)
                .build();
    }

    private User wrongUser() {
        return User.builder()
                .userId(USERID)
                .username(USERNAME)
                .password("wrong-password")
                .nickname(NICKNAME)
                .role(ROLE)
                .build();
    }

    private SessionUser sessionUser() {
        return SessionUser.builder()
                .userId(USERID)
                .username(USERNAME)
                .nickname(NICKNAME)
                .role(ROLE)
                .build();
    }
}