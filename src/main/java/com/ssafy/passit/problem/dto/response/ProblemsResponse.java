package com.ssafy.passit.problem.dto.response;

import com.ssafy.passit.problem.dto.entity.ProblemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ProblemResponse",
        description = "문제 ID, 제목, 타입을 페이지 단위로 리턴하는 DTO"
)
public class ProblemsResponse {

    @Schema(description = "문제 목록")
    private List<ProblemEntity> problems;

    @Schema(description = "마지막 페이지인지")
    private Boolean isLast;

    @Schema(description = "조회한 문제 중 가장 작은 id")
    private Long minProblemId;
}
