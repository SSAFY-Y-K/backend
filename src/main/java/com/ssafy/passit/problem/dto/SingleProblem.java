package com.ssafy.passit.problem.dto;

import com.ssafy.passit.common.type.ProblemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleProblem {

    @Schema(description = "문제 ID(요청 당시 null)")
    private Integer problemId;

    @Schema(description = "문제 셋 ID")
    private Integer problemSetId;

    @Schema(description = "문제 셋 내에서 해당 문제의 번호")
    private Integer problemNumber;

    @Schema(description = "문제 타입(MULTIPLE = 객관식, SHORT_ANSWER = 주관식, CODING = 코딩 문제")
    private ProblemType problemType;

    @Schema(description = "문제 내용")
    private String question;

    @Schema(description = "객관식 문제인 경우 정답인 선택지 번호, 주관식 문제면 null")
    private Integer answerCorrectNumber;

    @Schema(description = "주관식 문제인 경우 정답, 객관식 문제면 null")
    private String answerText;

    @Schema(description = "객관식 문제면 각 선택지 정보, 주관식 문제면 null")
    private List<ProblemChoice> problemChoices;
}



