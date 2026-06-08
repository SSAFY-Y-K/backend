package com.ssafy.passit.problem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemCreateRequest {

    @Schema(description = "문제를 만든 사용자 ID")
    private Integer userId;

    @Schema(description = "문제를 만들 자격증 Id")
    private Integer certId;
    
    @Schema(description = "문제 개수")
    private Integer problemCount;

    @Schema(description = "만든 문제들")
    private List<SingleProblem> problems;
}
