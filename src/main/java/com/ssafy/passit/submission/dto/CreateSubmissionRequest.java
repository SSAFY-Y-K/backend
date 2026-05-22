package com.ssafy.passit.submission.dto;

public record CreateSubmissionRequest(
    Long userId,
    String language,
    String sourceCode
) {
}
