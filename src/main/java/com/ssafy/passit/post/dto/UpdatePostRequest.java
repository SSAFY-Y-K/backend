package com.ssafy.passit.post.dto;

public record UpdatePostRequest(
    String category,
    String title,
    String content
) {}
