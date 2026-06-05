package com.ssafy.passit.user.controller;

import com.ssafy.passit.user.dto.LoginRequest;
import com.ssafy.passit.user.dto.SignupRequest;
import com.ssafy.passit.user.dto.SessionUser;
import com.ssafy.passit.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @Operation(summary = "회원 가입", description = "회원 가입 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "400",
                    description = "비밀번호 불일치, 혹은 이미 존재하는 유저네임이나 닉네임")
    })
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "유저네임 또는 비밀번호가 다름")
    })
    public ResponseEntity<Void> login(
            HttpSession session,
            @RequestBody LoginRequest loginRequest) {
        SessionUser sessionUser = userService.login(loginRequest);

        session.setAttribute("sessionUser", sessionUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();

        return ResponseEntity.noContent().build();
    }
}