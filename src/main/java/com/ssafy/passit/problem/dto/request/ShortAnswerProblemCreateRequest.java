package com.ssafy.passit.problem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ShortAnswerProblemCreateRequest",
        description = "주관식 문제 생성 요청 DTO"
)
public class ShortAnswerProblemCreateRequest implements AiResponse {
    @Schema(description = "문제를 생성하려는 자격증의 ID", example = "123")
    private Long certId;

    @Schema(description = "문제 제목", example = "간단한 사칙연산")
    private String title;

    @Schema(description = "문제", example = "1 + 1은?")
    private String question;

    @Schema(description = "정답", example = "2")
    private String answer;
}
