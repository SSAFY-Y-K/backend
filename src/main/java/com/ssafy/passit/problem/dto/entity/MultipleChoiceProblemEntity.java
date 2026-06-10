package com.ssafy.passit.problem.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultipleChoiceProblemEntity {

    private Long problemId;
    private String question;
    private String choice1Content;
    private String choice2Content;
    private String choice3Content;
    private String choice4Content;
    private Integer answerNumber;
}
