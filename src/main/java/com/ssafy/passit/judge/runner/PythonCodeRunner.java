package com.ssafy.passit.judge.runner;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.common.type.LanguageType;
import com.ssafy.passit.common.type.VerdictType;
import com.ssafy.passit.judge.dto.ExecutionRequest;
import com.ssafy.passit.judge.dto.ExecutionResult;
import com.ssafy.passit.judge.support.DockerCommandFactory;
import com.ssafy.passit.judge.support.ProcessExecutionResult;
import com.ssafy.passit.judge.support.ProcessExecutor;
import com.ssafy.passit.judge.support.WorkspaceManager;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PythonCodeRunner implements CodeRunner {

    private final WorkspaceManager workspaceManager;
    private final DockerCommandFactory dockerCommandFactory;
    private final ProcessExecutor processExecutor;

    @Override
    public LanguageType supportLanguage() {
        return LanguageType.PYTHON;
    }

    @Override
    public ExecutionResult run(ExecutionRequest request) {
        validateRequest(request);

        Path workspace = null;
        try {
            String wsKey = request.submissionId() != null ? "sub-" + request.submissionId() : "run-" + UUID.randomUUID();
            workspace = workspaceManager.createWorkspace(wsKey, LanguageType.PYTHON);
            workspaceManager.writeSourceFile(workspace, request.sourceCode(), LanguageType.PYTHON);

            List<String> command = dockerCommandFactory.buildPythonCommand(request, workspace);
            long timeoutMs = request.timeLimitMs() + 30_000L;

            ProcessExecutionResult processResult =
                processExecutor.execute(command, request.stdin(), timeoutMs);

            return toExecutionResult(processResult, request.timeLimitMs());
        } catch (IOException exception) {
            throw new ApiException(ErrorCode.INTERNAL_ERROR, "Python 실행용 임시 작업 디렉터리를 준비하지 못했습니다.");
        } finally {
            cleanup(workspace);
        }
    }

    private void validateRequest(ExecutionRequest request) {
        if (request == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "실행 요청이 비어 있습니다.");
        }
        if (request.language() != LanguageType.PYTHON) {
            throw new ApiException(ErrorCode.UNSUPPORTED_LANGUAGE, "Python Runner는 PYTHON 언어만 실행할 수 있습니다.");
        }
        if (request.problemId() == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "problemId는 필수입니다.");
        }
        if (request.sourceCode() == null || request.sourceCode().isBlank()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "실행할 Python 코드가 비어 있습니다.");
        }
        if (request.timeLimitMs() == null || request.timeLimitMs() <= 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "timeLimitMs는 0보다 커야 합니다.");
        }
        if (request.memoryLimitMb() == null || request.memoryLimitMb() <= 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "memoryLimitMb는 0보다 커야 합니다.");
        }
    }

    private ExecutionResult toExecutionResult(ProcessExecutionResult processResult, Integer timeLimitMs) {
        Integer execTimeMs = parseExecTimeMs(processResult.stderr());
        String cleanedStderr = cleanMarker(processResult.stderr());
        int displayTime = execTimeMs != null ? execTimeMs : processResult.execTimeMs();

        if (processResult.exitCode() == 137) {
            return new ExecutionResult(VerdictType.MLE, displayTime, null,
                processResult.stdout(), cleanedStderr, "메모리 제한을 초과했습니다.");
        }

        if (processResult.timedOut() || processResult.exitCode() == 124
                || (execTimeMs != null && execTimeMs > timeLimitMs)) {
            return new ExecutionResult(VerdictType.TLE, displayTime, null,
                processResult.stdout(), cleanedStderr, "실행 시간이 제한을 초과했습니다.");
        }

        if (processResult.exitCode() != 0) {
            return new ExecutionResult(VerdictType.RE, displayTime, null,
                processResult.stdout(), cleanedStderr,
                cleanedStderr != null && !cleanedStderr.isBlank() ? cleanedStderr : "Python 실행 중 오류가 발생했습니다.");
        }

        return new ExecutionResult(VerdictType.AC, displayTime, null,
            processResult.stdout(), cleanedStderr, null);
    }

    private Integer parseExecTimeMs(String stderr) {
        if (stderr == null) return null;
        for (String line : stderr.split("\n")) {
            String t = line.trim();
            if (t.startsWith(DockerCommandFactory.EXEC_TIME_MARKER)) {
                try {
                    return Integer.parseInt(t.substring(DockerCommandFactory.EXEC_TIME_MARKER.length()).trim());
                } catch (NumberFormatException ignored) {}
            }
        }
        return null;
    }

    private String cleanMarker(String stderr) {
        if (stderr == null) return null;
        return stderr.lines()
            .filter(l -> !l.trim().startsWith(DockerCommandFactory.EXEC_TIME_MARKER))
            .collect(Collectors.joining("\n"))
            .strip();
    }

    private void cleanup(Path workspace) {
        try {
            workspaceManager.deleteWorkspace(workspace);
        } catch (IOException ignored) {}
    }
}
