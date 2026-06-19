package com.ssafy.passit.user.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.post.dto.PostResponse;
import com.ssafy.passit.security.UserPrincipal;
import com.ssafy.passit.submission.dto.MySubmissionResponse;
import com.ssafy.passit.user.dto.SignupRequest;
import com.ssafy.passit.user.dto.UserProfileResponse;
import com.ssafy.passit.user.exception.SignupValidationException;
import com.ssafy.passit.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/me")
    @Operation(summary = "내 프로필 조회")
    public ApiResponse<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(userService.getProfile(principal.getUserId()));
    }

    @GetMapping("/me/posts")
    @Operation(summary = "내 게시글 목록 조회")
    public ApiResponse<List<PostResponse>> getMyPosts(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(
            userService.getMyPosts(principal.getUserId()).stream()
                .map(PostResponse::from)
                .toList()
        );
    }

    @GetMapping("/me/submissions")
    @Operation(summary = "내 제출 이력 조회")
    public ApiResponse<List<MySubmissionResponse>> getMySubmissions(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(userService.getMySubmissions(principal.getUserId()));
    }

    @ExceptionHandler(SignupValidationException.class)
    public ProblemDetail handleSignupValidationException(
            SignupValidationException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Signup Failed");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("errorField", exception.getErrorField());

        return problemDetail;
    }
}