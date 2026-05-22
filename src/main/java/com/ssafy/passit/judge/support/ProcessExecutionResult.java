package com.ssafy.passit.judge.support;

public record ProcessExecutionResult(
    int exitCode,
    boolean timedOut,
    int execTimeMs,
    String stdout,
    String stderr
) {
}
