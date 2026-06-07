package com.ssafy.passit.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenerateAlgorithmResponse {

    private Long problemId;
    private String title;
    private String category;
    private int testCaseCount;
}
