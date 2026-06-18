package com.ssafy.passit.judge.support;

import com.ssafy.passit.judge.dto.ExecutionRequest;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DockerCommandFactory {

    private static final String WORKSPACE_DIR = "/workspace";

    public List<String> buildPythonCommand(ExecutionRequest request, Path workspace) {
        int timeLimitSec = toSeconds(request.timeLimitMs());
        return buildBaseCommand(request, workspace, "python:3.11-alpine",
            List.of("sh", "-c", "timeout " + timeLimitSec + "s python3 main.py; ec=$?; if [ $ec -eq 124 ]; then exit 124; fi; exit $ec")
        );
    }

    public List<String> buildJavaCommand(ExecutionRequest request, Path workspace) {
        int timeLimitSec = toSeconds(request.timeLimitMs());
        return buildBaseCommand(
            request,
            workspace,
            "eclipse-temurin:21",
            List.of(
                "sh",
                "-c",
                "javac Main.java; status=$?; if [ $status -ne 0 ]; then echo '__PASSIT_COMPILE_ERROR__' >&2; exit 101; fi; timeout " + timeLimitSec + "s java Main; ec=$?; if [ $ec -eq 124 ]; then exit 124; fi; exit $ec"
            )
        );
    }

    public List<String> buildCppCommand(ExecutionRequest request, Path workspace) {
        int timeLimitSec = toSeconds(request.timeLimitMs());
        return buildBaseCommand(
            request,
            workspace,
            "gcc:13",
            List.of(
                "sh",
                "-c",
                "g++ -O2 -std=c++17 main.cpp -o main; status=$?; if [ $status -ne 0 ]; then echo '__PASSIT_COMPILE_ERROR__' >&2; exit 101; fi; timeout " + timeLimitSec + "s ./main; ec=$?; if [ $ec -eq 124 ]; then exit 124; fi; exit $ec"
            )
        );
    }

    private int toSeconds(Integer timeLimitMs) {
        if (timeLimitMs == null || timeLimitMs <= 0) return 5;
        return Math.max(1, (int) Math.ceil(timeLimitMs / 1000.0));
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
