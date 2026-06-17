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
        name = "MultipleChoiceProblemCreateRequest",
        description = "객관식 문제 생성 요청 DTO"
)
public class MultipleChoiceProblemCreateRequest implements AiResponse {
    @Schema(description = "생성하려는 자격증의 ID", example = "123")
    private Long certId;

    @Schema(description = "문제 제목", example = "간단한 사칙연산")
    private String title;

    @Schema(description = "문제 내용", example = "1 + 1은?")
    private String question;

    @Schema(description = "1번 선택지", example = "1")
    private String choice1Content;

    @Schema(description = "2번 선택지", example = "2")
    private String choice2Content;

    @Schema(description = "3번 선택지", example = "3")
    private String choice3Content;

    @Schema(description = "4번 선택지", example = "4")
    private String choice4Content;

    @Schema(description = "정답 선택지 번호", example = "2")
    private Integer answerNumber;
}
