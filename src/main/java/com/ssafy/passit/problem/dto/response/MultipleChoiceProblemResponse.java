package com.ssafy.passit.problem.dto.response;

import com.ssafy.passit.common.type.ProblemType;
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
        name = "MultipleChoiceProblemResponse",
        description = "객관식 문제 조회 DTO"
)
public class MultipleChoiceProblemResponse {

    @Schema(description = "문제 ID")
    private Long problemId;

    @Schema(description = "문제의 자격증 ID")
    private Long certId;

    @Schema(description = "문제의 자격증 이름")
    private String certificationName;

    @Schema(description = "문제 타입")
    private ProblemType problemType;

    @Schema(description = "문제 제목")
    private String problemTitle;

    @Schema(description = "문제 내용")
    private String question;

    @Schema(description = "1번 선택지")
    private String choice1Content;

    @Schema(description = "2번 선택지")
    private String choice2Content;

    @Schema(description = "3번 선택지")
    private String choice3Content;

    @Schema(description = "4번 선택지")
    private String choice4Content;

    @Schema(description = "정답 선택지 번호")
    private Integer answerNumber;
}
