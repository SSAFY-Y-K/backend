package com.ssafy.passit.post.dto;

import com.ssafy.passit.post.model.Post;
import java.time.LocalDateTime;

public record PostResponse(
    Long postId,
    Long userId,
    Long certId,
    String category,
    String title,
    Integer viewCount,
    LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getPostId(),
            post.getUserId(),
            post.getCertId(),
            post.getCategory(),
            post.getTitle(),
            post.getViewCount(),
            post.getCreatedAt()
        );
    }
}
