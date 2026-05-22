package com.ssafy.passit.judge.support;

public record ProcessExecutionResult(
    int exitCode,
    boolean timedOut,
    String stdout,
    String stderr
) {
}
