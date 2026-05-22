package com.ssafy.passit.judge.support;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProcessExecutor {

    public ProcessExecutionResult execute(List<String> command, String stdin, long timeoutMs) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "프로세스 실행 로직은 다음 단계에서 구현합니다.");
    }
}
