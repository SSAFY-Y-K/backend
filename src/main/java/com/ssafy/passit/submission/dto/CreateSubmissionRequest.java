package com.ssafy.passit.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코드 제출 요청")
public record CreateSubmissionRequest(
    @Schema(description = "사용자 ID", example = "1") Long userId,
    @Schema(description = "언어", allowableValues = {"PYTHON", "JAVA", "CPP"}, example = "PYTHON") String language,
    @Schema(description = "소스 코드", example = "n = int(input())\nprint(n * 2)") String sourceCode
) {
}
