package com.ssafy.passit.submission.dto;

import com.ssafy.passit.submission.model.Submission;

public record SubmissionSourceResponse(
    Long submissionId,
    Long problemId,
    String language,
    String sourceCode
) {

    public static SubmissionSourceResponse from(Submission submission) {
        return new SubmissionSourceResponse(
            submission.getSubmissionId(),
            submission.getProblemId(),
            submission.getLanguage() == null ? null : submission.getLanguage().name(),
            submission.getSourceCode()
        );
    }
}
