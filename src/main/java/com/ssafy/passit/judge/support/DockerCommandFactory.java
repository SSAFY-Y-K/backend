package com.ssafy.passit.judge.support;

import com.ssafy.passit.judge.dto.ExecutionRequest;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DockerCommandFactory {

    private static final String WORKSPACE_DIR = "/workspace";
    public static final String EXEC_TIME_MARKER = "__PASSIT_EXEC_MS__";
    private final String pythonImage;
    private final String javaImage;
    private final String cppImage;

    public DockerCommandFactory(
        @Value("${judge.image.python:python:3.11-slim}") String pythonImage,
        @Value("${judge.image.java:eclipse-temurin:21}") String javaImage,
        @Value("${judge.image.cpp:gcc:13}") String cppImage
    ) {
        this.pythonImage = pythonImage;
        this.javaImage = javaImage;
        this.cppImage = cppImage;
    }

    public List<String> buildPythonCommand(ExecutionRequest request, Path workspace) {
        String timeArg = toTimeoutArg(request.timeLimitMs());
        // python:3.11-slim(Debian) 사용 — Alpine BusyBox는 date +%s%N 미지원
        String script =
            "START=$(date +%s%N); " +
            "timeout " + timeArg + " python3 main.py; ec=$?; " +
            "END=$(date +%s%N); " +
            "echo '" + EXEC_TIME_MARKER + "'$(( (END-START)/1000000 )) >&2; " +
            "if [ $ec -eq 124 ]; then exit 124; fi; exit $ec";
        return buildBaseCommand(request, workspace, pythonImage, List.of("sh", "-c", script));
    }

    public List<String> buildJavaCommand(ExecutionRequest request, Path workspace) {
        String timeArg = toTimeoutArg(request.timeLimitMs());
        String script =
            "javac Main.java 2>/tmp/ce; s=$?; " +
            "if [ $s -ne 0 ]; then cat /tmp/ce >&2; echo '__PASSIT_COMPILE_ERROR__' >&2; exit 101; fi; " +
            "START=$(date +%s%N); " +
            "timeout " + timeArg + " java Main; ec=$?; " +
            "END=$(date +%s%N); " +
            "echo '" + EXEC_TIME_MARKER + "'$(( (END-START)/1000000 )) >&2; " +
            "if [ $ec -eq 124 ]; then exit 124; fi; exit $ec";
        return buildBaseCommand(request, workspace, javaImage, List.of("sh", "-c", script));
    }

    public List<String> buildCppCommand(ExecutionRequest request, Path workspace) {
        String timeArg = toTimeoutArg(request.timeLimitMs());
        String script =
            "g++ -O2 -std=c++17 main.cpp -o main 2>/tmp/ce; s=$?; " +
            "if [ $s -ne 0 ]; then cat /tmp/ce >&2; echo '__PASSIT_COMPILE_ERROR__' >&2; exit 101; fi; " +
            "START=$(date +%s%N); " +
            "timeout " + timeArg + " ./main; ec=$?; " +
            "END=$(date +%s%N); " +
            "echo '" + EXEC_TIME_MARKER + "'$(( (END-START)/1000000 )) >&2; " +
            "if [ $ec -eq 124 ]; then exit 124; fi; exit $ec";
        return buildBaseCommand(request, workspace, cppImage, List.of("sh", "-c", script));
    }

    private String toTimeoutArg(Integer timeLimitMs) {
        if (timeLimitMs == null || timeLimitMs <= 0) return "5s";
        return (timeLimitMs / 1000.0) + "s";
    }

    private List<String> buildBaseCommand(
        ExecutionRequest request,
        Path workspace,
        String image,
        List<String> runtimeCommand
    ) {
        List<String> command = new ArrayList<>();
        command.add("docker");
        command.add("run");
        command.add("--rm");
        command.add("-i");
        command.add("--network");
        command.add("none");
        command.add("--cpus");
        command.add("1");
        command.add("--memory");
        command.add(request.memoryLimitMb() + "m");
        command.add("--memory-swap");
        command.add(request.memoryLimitMb() + "m");
        command.add("--volume");
        command.add(workspace.toAbsolutePath() + ":" + WORKSPACE_DIR);
        command.add("--workdir");
        command.add(WORKSPACE_DIR);
        command.add(image);
        command.addAll(runtimeCommand);
        return command;
    }
}
