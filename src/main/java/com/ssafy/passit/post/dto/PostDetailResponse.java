package com.ssafy.passit.post.dto;

import com.ssafy.passit.post.model.Post;
import java.time.LocalDateTime;

public record PostDetailResponse(
    Long postId,
    Long userId,
    String nickname,
    Long certId,
    String category,
    String title,
    String content,
    Integer viewCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
            post.getPostId(),
            post.getUserId(),
            post.getNickname(),
            post.getCertId(),
            post.getCategory(),
            post.getTitle(),
            post.getContent(),
            post.getViewCount(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}
