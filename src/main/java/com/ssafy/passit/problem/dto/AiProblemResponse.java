package com.ssafy.passit.problem.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiProblemResponse {

    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private String constraintText;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String category;
    private List<AiTestCaseResponse> testCases;

    @Getter
    @NoArgsConstructor
    public static class AiTestCaseResponse {
        private String inputData;
        private String expectedOutput;
        private Boolean isSample;
        private Integer caseOrder;
    }
}
