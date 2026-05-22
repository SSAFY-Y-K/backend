package com.ssafy.passit.submission.dto;

import com.ssafy.passit.submission.model.Submission;
import java.time.LocalDateTime;

public record SubmissionResultResponse(
    Long submissionId,
    Long problemId,
    Long userId,
    String language,
    String status,
    String verdict,
    Integer execTimeMs,
    Integer memoryKb,
    String errorMessage,
    LocalDateTime submittedAt,
    LocalDateTime judgedAt
) {

    public static SubmissionResultResponse from(Submission submission) {
        return new SubmissionResultResponse(
            submission.getSubmissionId(),
            submission.getProblemId(),
            submission.getUserId(),
            submission.getLanguage() == null ? null : submission.getLanguage().name(),
            submission.getStatus() == null ? null : submission.getStatus().name(),
            submission.getVerdict() == null ? null : submission.getVerdict().name(),
            submission.getExecTimeMs(),
            submission.getMemoryKb(),
            submission.getErrorMessage(),
            submission.getSubmittedAt(),
            submission.getJudgedAt()
        );
    }
}
