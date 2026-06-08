package com.ssafy.passit.problem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemChoice {

    @Schema(description = "문제 ID")
    private Integer problemId;

    @Schema(description = "해당 선택지 번호")
    private Integer choiceNumber;

    @Schema(description = "선택지 내용")
    private String content;
}
