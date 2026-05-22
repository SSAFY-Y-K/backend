package com.ssafy.passit.judge.runner;

import com.ssafy.passit.common.type.LanguageType;
import com.ssafy.passit.judge.dto.ExecutionRequest;
import com.ssafy.passit.judge.dto.ExecutionResult;

public interface CodeRunner {

    LanguageType supportLanguage();

    ExecutionResult run(ExecutionRequest request);
}
