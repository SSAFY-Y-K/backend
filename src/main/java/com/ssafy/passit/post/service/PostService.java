package com.ssafy.passit.post.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.post.dto.CreatePostRequest;
import com.ssafy.passit.post.dto.PostDetailResponse;
import com.ssafy.passit.post.dto.PostResponse;
import com.ssafy.passit.post.dto.UpdatePostRequest;
import com.ssafy.passit.post.mapper.PostMapper;
import com.ssafy.passit.post.model.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    @Transactional
    public PostDetailResponse createPost(CreatePostRequest request) {
        Post post = Post.builder()
            .userId(request.userId())
            .certId(request.certId())
            .category(request.category())
            .title(request.title())
            .content(request.content())
            .build();

        postMapper.insertPost(post);
        return PostDetailResponse.from(findById(post.getPostId()));
    }

    public List<PostResponse> getPosts() {
        return postMapper.findAll().stream()
            .map(PostResponse::from)
            .toList();
    }

    @Transactional
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = findById(postId);
        postMapper.incrementViewCount(postId);
        post.setViewCount(post.getViewCount() + 1);
        return PostDetailResponse.from(post);
    }

    @Transactional
    public PostDetailResponse updatePost(Long postId, UpdatePostRequest request) {
        Post post = findById(postId);
        post.setCategory(request.category());
        post.setTitle(request.title());
        post.setContent(request.content());

        postMapper.updatePost(post);
        return PostDetailResponse.from(findById(postId));
    }

    @Transactional
    public void deletePost(Long postId) {
        findById(postId);
        postMapper.deletePost(postId);
    }

    private Post findById(Long postId) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new ApiException(ErrorCode.POST_NOT_FOUND);
        }
        return post;
    }
}
