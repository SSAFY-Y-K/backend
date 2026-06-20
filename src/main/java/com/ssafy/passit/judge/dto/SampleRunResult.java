package com.ssafy.passit.judge.dto;

public record SampleRunResult(
    int caseOrder,
    String inputData,
    String expectedOutput,
    String actualOutput,
    boolean correct,
    String verdict,
    Integer execTimeMs,
    String errorMessage
) {}
