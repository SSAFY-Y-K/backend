package com.ssafy.passit.post.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.post.dto.CreatePostRequest;
import com.ssafy.passit.post.dto.PostDetailResponse;
import com.ssafy.passit.post.dto.PostListResponse;
import com.ssafy.passit.post.dto.PostResponse;
import com.ssafy.passit.post.dto.UpdatePostRequest;
import com.ssafy.passit.post.mapper.PostMapper;
import com.ssafy.passit.post.model.Post;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostMapper postMapper;

    @Transactional
    public PostDetailResponse createPost(Long actorUserId, CreatePostRequest request) {
        log.info("Post creation requested. actorUserId={}, certId={}, category={}",
            actorUserId, request.certId(), request.category());

        Post post = Post.builder()
            .userId(actorUserId)
            .certId(request.certId())
            .category(request.category())
            .title(request.title())
            .content(request.content())
            .build();

        postMapper.insertPost(post);
        log.info("Post created. postId={}, userId={}, certId={}",
            post.getPostId(), post.getUserId(), post.getCertId());
        return PostDetailResponse.from(findById(post.getPostId()));
    }

    public PostListResponse getPosts(String keyword, int page, int size) {
        int offset = page * size;
        List<PostResponse> posts = postMapper.findAll(keyword, offset, size).stream()
            .map(PostResponse::from)
            .toList();
        int total = postMapper.countAll(keyword);
        return new PostListResponse(posts, total, page, size);
    }

    @Transactional
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = findById(postId);
        postMapper.incrementViewCount(postId);
        post.setViewCount(post.getViewCount() + 1);
        log.debug("Post viewed. postId={}, viewCount={}", postId, post.getViewCount());
        return PostDetailResponse.from(post);
    }

    @Transactional
    public PostDetailResponse updatePost(
        Long postId,
        Long actorUserId,
        boolean isAdmin,
        UpdatePostRequest request
    ) {
        log.info("Post update requested. postId={}, actorUserId={}, category={}",
            postId, actorUserId, request.category());

        Post post = findById(postId);
        validateOwnerOrAdmin(
            post.getUserId(),
            actorUserId,
            isAdmin,
            "Only the owner or an admin can update this post."
        );

        post.setCategory(request.category());
        post.setTitle(request.title());
        post.setContent(request.content());

        postMapper.updatePost(post);
        log.info("Post updated. postId={}, actorUserId={}", postId, actorUserId);
        return PostDetailResponse.from(findById(postId));
    }

    @Transactional
    public void deletePost(Long postId, Long actorUserId, boolean isAdmin) {
        Post post = findById(postId);
        validateOwnerOrAdmin(
            post.getUserId(),
            actorUserId,
            isAdmin,
            "Only the owner or an admin can delete this post."
        );

        postMapper.deletePost(postId);
        log.info("Post deleted. postId={}, actorUserId={}", postId, actorUserId);
    }

    private Post findById(Long postId) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            log.warn("Post not found. postId={}", postId);
            throw new ApiException(ErrorCode.POST_NOT_FOUND);
        }
        return post;
    }

    private void validateOwnerOrAdmin(
        Long ownerUserId,
        Long actorUserId,
        boolean isAdmin,
        String message
    ) {
        if (isAdmin) {
            return;
        }
        if (!Objects.equals(ownerUserId, actorUserId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, message);
        }
    }
}
