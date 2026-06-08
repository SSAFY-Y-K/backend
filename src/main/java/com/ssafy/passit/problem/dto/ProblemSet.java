package com.ssafy.passit.problem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemSet {

    private Integer problemSetId;
    private Integer certId;
    private Integer userId;
}
