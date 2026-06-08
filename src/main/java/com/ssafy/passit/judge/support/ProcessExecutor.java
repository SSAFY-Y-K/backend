package com.ssafy.passit.judge.support;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProcessExecutor {

    public ProcessExecutionResult execute(List<String> command, String stdin, long timeoutMs) {
        validate(command, timeoutMs);

        Process process;
        long startedAt = System.nanoTime();
        try {
            process = new ProcessBuilder(command).start();
        } catch (IOException exception) {
            throw new ApiException(ErrorCode.INTERNAL_ERROR, "외부 프로세스를 시작하지 못했습니다.");
        }

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> stdoutFuture = executor.submit(() -> readStream(process.getInputStream()));
            Future<String> stderrFuture = executor.submit(() -> readStream(process.getErrorStream()));

            writeStdin(process, stdin);

            boolean finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
            if (!finished) {
                process.destroyForcibly();
                process.waitFor(5, TimeUnit.SECONDS);

                String stdout = "";
                String stderr = "";
                try {
                    stdout = getFutureValue(stdoutFuture);
                    stderr = getFutureValue(stderrFuture);
                } catch (Exception ignored) {
                    // 강제 종료 시 스트림 읽기 실패는 무시
                }

                return new ProcessExecutionResult(-1, true, elapsedMillis(startedAt), stdout, stderr);
            }

            return new ProcessExecutionResult(
                process.exitValue(),
                false,
                elapsedMillis(startedAt),
                getFutureValue(stdoutFuture),
                getFutureValue(stderrFuture)
            );
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ApiException(ErrorCode.INTERNAL_ERROR, "프로세스 실행이 중단되었습니다.");
        }
    }

    private void validate(List<String> command, long timeoutMs) {
        if (command == null || command.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "실행 명령이 비어 있습니다.");
        }

        if (timeoutMs <= 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "timeout 값은 0보다 커야 합니다.");
        }
    }

    private void writeStdin(Process process, String stdin) {
        try (Writer writer = new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8)) {
            if (stdin != null && !stdin.isEmpty()) {
                writer.write(stdin);
                writer.flush();
            }
        } catch (IOException ignored) {
            // 컴파일 오류처럼 프로세스가 먼저 종료된 경우가 있어 입력 실패를 여기서 막지 않는다.
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private String getFutureValue(Future<String> future) throws InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException exception) {
            throw new ApiException(ErrorCode.INTERNAL_ERROR, "프로세스 출력 읽기에 실패했습니다.");
        }
    }

    private int elapsedMillis(long startedAt) {
        return (int) ((System.nanoTime() - startedAt) / 1_000_000L);
    }
}
