package com.ssafy.passit.post.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.post.dto.CommentResponse;
import com.ssafy.passit.post.dto.CreateCommentRequest;
import com.ssafy.passit.post.mapper.CommentMapper;
import com.ssafy.passit.post.model.Comment;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public List<CommentResponse> getComments(Long postId) {
        return commentMapper.findByPostId(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse createComment(Long postId, Long actorUserId, CreateCommentRequest request) {
        Comment comment = Comment.builder()
                .postId(postId)
                .userId(actorUserId)
                .content(request.content())
                .build();
        commentMapper.insertComment(comment);
        return CommentResponse.from(commentMapper.findById(comment.getCommentId()));
    }

    @Transactional
    public void deleteComment(Long commentId, Long actorUserId, boolean isAdmin) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            throw new ApiException(ErrorCode.POST_NOT_FOUND, "?볤???李얠쓣 ???놁뒿?덈떎.");
        }
        validateOwnerOrAdmin(
                comment.getUserId(),
                actorUserId,
                isAdmin,
                "Only the owner or an admin can delete this comment."
        );
        commentMapper.deleteComment(commentId);
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
