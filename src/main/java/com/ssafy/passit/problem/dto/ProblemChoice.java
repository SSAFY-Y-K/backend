package com.ssafy.passit.problem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemChoice {

    private Integer problemId;

    private Integer choiceNumber;

    private String content;
}
