package com.ssafy.passit.problem.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortAnswerProblemEntity {

    private Long problemId;
    private String question;
    private String answer;
}