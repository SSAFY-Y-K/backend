package com.ssafy.passit.problem.dto.response;

import com.ssafy.passit.problem.model.CodingProblem;
import com.ssafy.passit.problem.model.TestCase;
import java.util.List;

public record CodingProblemDetailResponse(
        Long problemId,
        String title,
        String description,
        String inputDescription,
        String outputDescription,
        String constraintText,
        Integer timeLimit,
        Integer memoryLimit,
        String difficulty,
        String category,
        List<SampleTestCase> sampleTestCases
) {
    public record SampleTestCase(String inputData, String expectedOutput, Integer caseOrder) {
        public static SampleTestCase from(TestCase tc) {
            return new SampleTestCase(tc.getInputData(), tc.getExpectedOutput(), tc.getCaseOrder());
        }
    }

    public static CodingProblemDetailResponse from(CodingProblem problem, List<TestCase> sampleTestCases) {
        return new CodingProblemDetailResponse(
                problem.getProblemId(),
                problem.getTitle(),
                problem.getDescription(),
                problem.getInputDescription(),
                problem.getOutputDescription(),
                problem.getConstraintText(),
                problem.getTimeLimit(),
                problem.getMemoryLimit(),
                problem.getDifficulty(),
                problem.getCategory(),
                sampleTestCases.stream().map(SampleTestCase::from).toList()
        );
    }
}
