package com.ssafy.passit.problem.dto.entity;

import com.ssafy.passit.common.type.ProblemType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemEntity {

    private Long problemId;
    private Long certId;
    private ProblemType problemType;
}
