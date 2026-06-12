package com.ssafy.passit.problem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "AI 코딩 문제 생성 요청")
@Getter
@NoArgsConstructor
public class GenerateAlgorithmRequest {

    @Schema(description = "난이도", allowableValues = {"EASY", "MEDIUM", "HARD"}, defaultValue = "MEDIUM", example = "MEDIUM")
    private String difficulty = "MEDIUM";

    @Schema(description = "문제 유형 (자유 입력)", example = "구현")
    private String category = "구현";
}
