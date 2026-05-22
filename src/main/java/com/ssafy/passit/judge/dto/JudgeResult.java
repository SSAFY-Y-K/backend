package com.ssafy.passit.judge.dto;

import com.ssafy.passit.common.type.SubmissionStatus;
import com.ssafy.passit.common.type.VerdictType;

public record JudgeResult(
    Long submissionId,
    SubmissionStatus status,
    VerdictType verdict,
    Integer execTimeMs,
    Integer memoryKb,
    String errorMessage
) {
}
