package com.ssafy.passit.judge.dto;

import com.ssafy.passit.common.type.LanguageType;

public record ExecutionRequest(
    Long submissionId,
    Long problemId,
    LanguageType language,
    String sourceCode,
    String stdin,
    Integer timeLimitMs,
    Integer memoryLimitMb
) {
}
