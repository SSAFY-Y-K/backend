package com.ssafy.passit.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 작성 요청")
public record CreatePostRequest(
    @Schema(description = "사용자 ID", example = "1") Long userId,
    @Schema(description = "자격증 ID", example = "1") Long certId,
    @Schema(description = "카테고리", allowableValues = {"REVIEW", "TIP", "QNA"}, example = "TIP") String category,
    @Schema(description = "제목", example = "정보처리기사 합격 후기") String title,
    @Schema(description = "내용", example = "열심히 했더니 합격했습니다.") String content
) {}
