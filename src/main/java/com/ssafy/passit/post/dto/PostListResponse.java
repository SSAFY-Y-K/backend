package com.ssafy.passit.post.dto;

import java.util.List;

public record PostListResponse(
    List<PostResponse> posts,
    int totalCount,
    int page,
    int size
) {}
