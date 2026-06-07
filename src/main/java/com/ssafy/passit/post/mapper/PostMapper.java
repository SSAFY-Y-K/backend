package com.ssafy.passit.post.mapper;

import com.ssafy.passit.post.model.Post;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper {

    int insertPost(Post post);

    Post findById(@Param("postId") Long postId);

    List<Post> findAll();

    int updatePost(Post post);

    int incrementViewCount(@Param("postId") Long postId);

    int deletePost(@Param("postId") Long postId);
}
