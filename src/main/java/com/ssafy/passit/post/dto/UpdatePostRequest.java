package com.ssafy.passit.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 수정 요청")
public record UpdatePostRequest(
    @Schema(description = "카테고리", allowableValues = {"REVIEW", "TIP", "QNA"}, example = "REVIEW") String category,
    @Schema(description = "제목", example = "수정된 제목") String title,
    @Schema(description = "내용", example = "수정된 내용입니다.") String content
) {}
