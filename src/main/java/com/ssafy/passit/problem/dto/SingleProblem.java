package com.ssafy.passit.problem.dto;

import com.ssafy.passit.common.type.ProblemType;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleProblem {

    private Integer problemId;

    private Integer problemSetId;

    private Integer problemNumber;

    private ProblemType problemType;

    private String question;

    private Integer answerCorrectNumber;

    private String answerText;

    private List<ProblemChoice> problemChoices;
}



