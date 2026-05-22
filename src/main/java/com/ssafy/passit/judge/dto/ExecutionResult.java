package com.ssafy.passit.judge.dto;

import com.ssafy.passit.common.type.VerdictType;

public record ExecutionResult(
    VerdictType verdict,
    Integer execTimeMs,
    Integer memoryKb,
    String stdout,
    String stderr,
    String errorMessage
) {
}
