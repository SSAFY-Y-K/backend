package com.ssafy.passit.post.dto;

public record CreatePostRequest(
    Long userId,
    Long certId,
    String category,
    String title,
    String content
) {}
