package com.ssafy.passit.problem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemCreateFromAiRequest {

    @Schema(description = "생성할 자격증의 ID")
    private Integer certId;

    @Schema(description = "생성하는 사용자 ID")
    private Integer userId;

    @Schema(description = "생성할 문제 수")
    private Integer problemCount;
}
