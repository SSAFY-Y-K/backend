package com.ssafy.passit.post.mapper;

import com.ssafy.passit.post.model.Comment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
    void insertComment(Comment comment);
    List<Comment> findByPostId(@Param("postId") Long postId);
    Comment findById(@Param("commentId") Long commentId);
    void deleteComment(@Param("commentId") Long commentId);
}
