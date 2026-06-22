package com.ssafy.passit.post.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.post.dto.CommentResponse;
import com.ssafy.passit.post.dto.CreateCommentRequest;
import com.ssafy.passit.post.service.CommentService;
import com.ssafy.passit.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comments", description = "?볤? API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "?볤? 紐⑸줉 議고쉶")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ApiResponse.success(commentService.getComments(postId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "?볤? ?묒꽦")
    public ApiResponse<CommentResponse> createComment(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable Long postId,
        @RequestBody CreateCommentRequest request
    ) {
        return ApiResponse.success(commentService.createComment(postId, principal.getUserId(), request));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "?볤? ??젣")
    public void deleteComment(
        @AuthenticationPrincipal UserPrincipal principal,
        Authentication authentication,
        @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId, principal.getUserId(), isAdmin(authentication));
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
    }
}
