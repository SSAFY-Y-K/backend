package com.ssafy.passit.user.controller;

import com.ssafy.passit.user.dto.SignupRequest;
import com.ssafy.passit.user.exception.SignupValidationException;
import com.ssafy.passit.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("signup")
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

    @ExceptionHandler(SignupValidationException.class)
    public ProblemDetail handleSignupValidationException(
            SignupValidationException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Field");
        problemDetail.setDetail("Signup is failed");

        return problemDetail;
    }
}