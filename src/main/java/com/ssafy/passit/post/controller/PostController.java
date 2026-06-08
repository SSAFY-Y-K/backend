package com.ssafy.passit.post.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.post.dto.CreatePostRequest;
import com.ssafy.passit.post.dto.PostDetailResponse;
import com.ssafy.passit.post.dto.PostResponse;
import com.ssafy.passit.post.dto.UpdatePostRequest;
import com.ssafy.passit.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostDetailResponse> createPost(@RequestBody CreatePostRequest request) {
        return ApiResponse.success(postService.createPost(request));
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {
        return ApiResponse.success(postService.getPosts());
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        return ApiResponse.success(postService.getPostDetail(postId));
    }

    @PutMapping("/{postId}")
    public ApiResponse<PostDetailResponse> updatePost(
        @PathVariable Long postId,
        @RequestBody UpdatePostRequest request
    ) {
        return ApiResponse.success(postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }
}
