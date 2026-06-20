package com.ssafy.passit.post.dto;

import com.ssafy.passit.post.model.Comment;
import java.time.LocalDateTime;

public record CommentResponse(
    Long commentId,
    Long userId,
    String nickname,
    String content,
    LocalDateTime createdAt
) {
    public static CommentResponse from(Comment c) {
        return new CommentResponse(c.getCommentId(), c.getUserId(), c.getNickname(), c.getContent(), c.getCreatedAt());
    }
}
