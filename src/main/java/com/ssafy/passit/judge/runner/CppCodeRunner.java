package com.ssafy.passit.judge.runner;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.common.type.LanguageType;
import com.ssafy.passit.judge.dto.ExecutionRequest;
import com.ssafy.passit.judge.dto.ExecutionResult;
import com.ssafy.passit.judge.support.DockerCommandFactory;
import com.ssafy.passit.judge.support.ProcessExecutor;
import com.ssafy.passit.judge.support.WorkspaceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CppCodeRunner implements CodeRunner {

    private final WorkspaceManager workspaceManager;
    private final DockerCommandFactory dockerCommandFactory;
    private final ProcessExecutor processExecutor;

    @Override
    public LanguageType supportLanguage() {
        return LanguageType.CPP;
    }

    @Override
    public ExecutionResult run(ExecutionRequest request) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "C++ Runner 실행 로직은 다음 단계에서 구현합니다.");
    }
}
