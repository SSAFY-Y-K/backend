package com.ssafy.passit.report.dto;

import com.ssafy.passit.problem.model.TestCase;

public record TestCaseDetailResponse(
    Long testCaseId,
    String inputData,
    String expectedOutput,
    boolean isSample,
    int caseOrder
) {
    public static TestCaseDetailResponse from(TestCase tc) {
        return new TestCaseDetailResponse(
            tc.getTestCaseId(),
            tc.getInputData(),
            tc.getExpectedOutput(),
            Boolean.TRUE.equals(tc.getIsSample()),
            tc.getCaseOrder()
        );
    }
}
