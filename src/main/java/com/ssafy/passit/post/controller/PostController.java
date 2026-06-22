package com.ssafy.passit.post.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.post.dto.CreatePostRequest;
import com.ssafy.passit.post.dto.PostDetailResponse;
import com.ssafy.passit.post.dto.PostListResponse;
import com.ssafy.passit.post.dto.UpdatePostRequest;
import com.ssafy.passit.post.service.PostService;
import com.ssafy.passit.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Posts", description = "寃뚯떆湲 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "寃뚯떆湲 ?묒꽦", description = "??寃뚯떆湲???묒꽦?⑸땲?? category??REVIEW / TIP / QNA 以??섎굹?낅땲??")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "寃뚯떆湲 ?묒꽦 ?깃났")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostDetailResponse> createPost(
        @AuthenticationPrincipal UserPrincipal principal,
        @RequestBody CreatePostRequest request
    ) {
        return ApiResponse.success(postService.createPost(principal.getUserId(), request));
    }

    @Operation(summary = "寃뚯떆湲 紐⑸줉 議고쉶", description = "?꾩껜 寃뚯떆湲 紐⑸줉??議고쉶?⑸땲??")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "議고쉶 ?깃났")
    })
    @GetMapping
    public ApiResponse<PostListResponse> getPosts(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(postService.getPosts(keyword, page, size));
    }

    @Operation(summary = "寃뚯떆湲 ?곸꽭 議고쉶", description = "?뱀젙 寃뚯떆湲???곸꽭 ?댁슜??議고쉶?⑸땲??")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "議고쉶 ?깃났"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "寃뚯떆湲 ?놁쓬")
    })
    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostDetail(
        @Parameter(description = "寃뚯떆湲 ID") @PathVariable Long postId
    ) {
        return ApiResponse.success(postService.getPostDetail(postId));
    }

    @Operation(summary = "寃뚯떆湲 ?섏젙", description = "?뱀젙 寃뚯떆湲???섏젙?⑸땲??")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "?섏젙 ?깃났"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "寃뚯떆湲 ?놁쓬")
    })
    @PutMapping("/{postId}")
    public ApiResponse<PostDetailResponse> updatePost(
        @AuthenticationPrincipal UserPrincipal principal,
        Authentication authentication,
        @Parameter(description = "寃뚯떆湲 ID") @PathVariable Long postId,
        @RequestBody UpdatePostRequest request
    ) {
        return ApiResponse.success(
            postService.updatePost(postId, principal.getUserId(), isAdmin(authentication), request)
        );
    }

    @Operation(summary = "寃뚯떆湲 ??젣", description = "?뱀젙 寃뚯떆湲????젣?⑸땲??")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "??젣 ?깃났"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "寃뚯떆湲 ?놁쓬")
    })
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
        @AuthenticationPrincipal UserPrincipal principal,
        Authentication authentication,
        @Parameter(description = "寃뚯떆湲 ID") @PathVariable Long postId
    ) {
        postService.deletePost(postId, principal.getUserId(), isAdmin(authentication));
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
    }
}
