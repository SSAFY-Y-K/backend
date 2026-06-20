package com.ssafy.passit.post.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.post.dto.CommentResponse;
import com.ssafy.passit.post.dto.CreateCommentRequest;
import com.ssafy.passit.post.mapper.CommentMapper;
import com.ssafy.passit.post.model.Comment;
import java.util.List;
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
    public CommentResponse createComment(Long postId, CreateCommentRequest request) {
        Comment comment = Comment.builder()
                .postId(postId)
                .userId(request.userId())
                .content(request.content())
                .build();
        commentMapper.insertComment(comment);
        return CommentResponse.from(commentMapper.findById(comment.getCommentId()));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) throw new ApiException(ErrorCode.POST_NOT_FOUND, "댓글을 찾을 수 없습니다.");
        commentMapper.deleteComment(commentId);
    }
}
