package com.ssafy.passit.problem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    private Long problemId;
    private Long problemSetId;
    private Integer problemNumber;
    private String problemType;
    private String question;
    private Integer answerCorrectNumber;
    private String answerText;
}
