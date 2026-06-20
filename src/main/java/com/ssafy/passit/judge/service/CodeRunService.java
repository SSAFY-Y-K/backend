package com.ssafy.passit.judge.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.common.type.LanguageType;
import com.ssafy.passit.common.type.VerdictType;
import com.ssafy.passit.judge.dto.ExecutionRequest;
import com.ssafy.passit.judge.dto.ExecutionResult;
import com.ssafy.passit.judge.dto.RunCodeRequest;
import com.ssafy.passit.judge.dto.SampleRunResult;
import com.ssafy.passit.judge.runner.CodeRunner;
import com.ssafy.passit.judge.support.OutputComparator;
import com.ssafy.passit.problem.model.CodingProblem;
import com.ssafy.passit.problem.model.TestCase;
import com.ssafy.passit.problem.service.ProblemQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeRunService {

    private final ProblemQueryService problemQueryService;
    private final List<CodeRunner> codeRunners;
    private final OutputComparator outputComparator;

    public List<SampleRunResult> run(Long problemId, RunCodeRequest request) {
        CodingProblem problem = problemQueryService.getCodingProblem(problemId);
        List<TestCase> sampleCases = problemQueryService.getSampleTestCases(problemId);

        LanguageType language;
        try {
            language = LanguageType.valueOf(request.language().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.UNSUPPORTED_LANGUAGE, "지원하지 않는 언어입니다: " + request.language());
        }

        CodeRunner runner = codeRunners.stream()
            .filter(r -> r.supportLanguage() == language)
            .findFirst()
            .orElseThrow(() -> new ApiException(ErrorCode.UNSUPPORTED_LANGUAGE));

        return sampleCases.stream().map(tc -> {
            ExecutionRequest execReq = new ExecutionRequest(
                null,
                problemId,
                language,
                request.sourceCode(),
                tc.getInputData(),
                problem.getTimeLimit(),
                problem.getMemoryLimit()
            );
            ExecutionResult result = runner.run(execReq);

            String verdict;
            boolean correct;
            if (result.verdict() == VerdictType.AC) {
                correct = outputComparator.isMatch(tc.getExpectedOutput(), result.stdout());
                verdict = correct ? "AC" : "WA";
            } else {
                correct = false;
                verdict = result.verdict().name();
            }

            return new SampleRunResult(
                tc.getCaseOrder(),
                tc.getInputData(),
                tc.getExpectedOutput(),
                result.stdout(),
                correct,
                verdict,
                result.execTimeMs(),
                result.errorMessage()
            );
        }).toList();
    }
}
