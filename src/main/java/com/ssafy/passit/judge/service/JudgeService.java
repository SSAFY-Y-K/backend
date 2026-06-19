package com.ssafy.passit.judge.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.common.type.SubmissionStatus;
import com.ssafy.passit.common.type.VerdictType;
import com.ssafy.passit.judge.dto.ExecutionRequest;
import com.ssafy.passit.judge.dto.ExecutionResult;
import com.ssafy.passit.judge.dto.JudgeResult;
import com.ssafy.passit.judge.runner.CodeRunner;
import com.ssafy.passit.judge.support.OutputComparator;
import com.ssafy.passit.problem.model.CodingProblem;
import com.ssafy.passit.problem.model.TestCase;
import com.ssafy.passit.problem.service.ProblemQueryService;
import com.ssafy.passit.submission.model.Submission;
import java.time.LocalDateTime;
import com.ssafy.passit.submission.mapper.SubmissionMapper;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgeService {

    private final SubmissionMapper submissionMapper;
    private final ProblemQueryService problemQueryService;
    private final List<CodeRunner> codeRunners;
    private final OutputComparator outputComparator;

    @Transactional
    public JudgeResult judge(Long submissionId) {
        Submission submission = findSubmission(submissionId);
        log.info("Judge started. submissionId={}, problemId={}, language={}",
            submissionId, submission.getProblemId(), submission.getLanguage());

        try {
            updateStatusRunning(submissionId);

            CodingProblem problem = problemQueryService.getCodingProblem(submission.getProblemId());
            List<TestCase> hiddenTestCases = problemQueryService.getHiddenTestCases(problem.getProblemId());
            CodeRunner codeRunner = findCodeRunner(submission);

            JudgeResult judgeResult = executeTestCases(submission, problem, hiddenTestCases, codeRunner);
            saveJudgeSuccess(judgeResult);
            log.info("Judge completed. submissionId={}, verdict={}, execTimeMs={}, memoryKb={}",
                submissionId, judgeResult.verdict(), judgeResult.execTimeMs(), judgeResult.memoryKb());
            return judgeResult;
        } catch (ApiException exception) {
            log.warn("Judge failed by API exception. submissionId={}, message={}",
                submissionId, exception.getMessage());
            markFailed(submissionId, exception.getMessage());
            return new JudgeResult(
                submissionId,
                SubmissionStatus.FAILED,
                null,
                null,
                null,
                exception.getMessage()
            );
        } catch (Exception exception) {
            log.error("Judge failed by unexpected exception. submissionId={}", submissionId, exception);
            markFailed(submissionId, "채점 중 알 수 없는 오류가 발생했습니다.");
            return new JudgeResult(
                submissionId,
                SubmissionStatus.FAILED,
                null,
                null,
                null,
                "채점 중 알 수 없는 오류가 발생했습니다."
            );
        }
    }

    private Submission findSubmission(Long submissionId) {
        Submission submission = submissionMapper.findById(submissionId);

        if (submission == null) {
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }

        return submission;
    }

    private void updateStatusRunning(Long submissionId) {
        int updatedCount = submissionMapper.updateStatusRunning(submissionId);

        if (updatedCount == 0) {
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
    }

    private CodeRunner findCodeRunner(Submission submission) {
        return codeRunners.stream()
            .filter(runner -> runner.supportLanguage() == submission.getLanguage())
            .findFirst()
            .orElseThrow(() -> new ApiException(ErrorCode.UNSUPPORTED_LANGUAGE));
    }

    private JudgeResult executeTestCases(
        Submission submission,
        CodingProblem problem,
        List<TestCase> hiddenTestCases,
        CodeRunner codeRunner
    ) {
        int maxExecTime = 0;
        Integer maxMemoryKb = null;

        for (TestCase testCase : hiddenTestCases) {
            log.debug("Executing test case. submissionId={}, problemId={}, caseOrder={}",
                submission.getSubmissionId(), problem.getProblemId(), testCase.getCaseOrder());
            ExecutionRequest executionRequest = new ExecutionRequest(
                submission.getSubmissionId(),
                problem.getProblemId(),
                submission.getLanguage(),
                submission.getSourceCode(),
                testCase.getInputData(),
                problem.getTimeLimit(),
                problem.getMemoryLimit()
            );

            ExecutionResult executionResult = codeRunner.run(executionRequest);
            maxExecTime = Math.max(maxExecTime, defaultIfNull(executionResult.execTimeMs()));
            maxMemoryKb = max(maxMemoryKb, executionResult.memoryKb());

            if (executionResult.verdict() == VerdictType.AC) {
                boolean matched = outputComparator.isMatch(testCase.getExpectedOutput(), executionResult.stdout());
                if (!matched) {
                    log.info("Judge test case mismatch. submissionId={}, caseOrder={}",
                        submission.getSubmissionId(), testCase.getCaseOrder());
                    return new JudgeResult(
                        submission.getSubmissionId(),
                        SubmissionStatus.DONE,
                        VerdictType.WA,
                        maxExecTime,
                        maxMemoryKb,
                        testCase.getCaseOrder() + "번 숨은 테스트케이스에서 정답과 출력이 일치하지 않습니다."
                    );
                }

                continue;
            }

            log.info("Judge stopped on non-AC verdict. submissionId={}, caseOrder={}, verdict={}",
                submission.getSubmissionId(), testCase.getCaseOrder(), executionResult.verdict());
            return new JudgeResult(
                submission.getSubmissionId(),
                SubmissionStatus.DONE,
                executionResult.verdict(),
                maxExecTime,
                maxMemoryKb,
                prependCaseOrder(testCase, executionResult.errorMessage())
            );
        }

        return new JudgeResult(
            submission.getSubmissionId(),
            SubmissionStatus.DONE,
            VerdictType.AC,
            maxExecTime,
            maxMemoryKb,
            null
        );
    }

    private void saveJudgeSuccess(JudgeResult judgeResult) {
        Submission submission = Submission.builder()
            .submissionId(judgeResult.submissionId())
            .status(judgeResult.status())
            .verdict(judgeResult.verdict())
            .execTimeMs(judgeResult.execTimeMs())
            .memoryKb(judgeResult.memoryKb())
            .errorMessage(judgeResult.errorMessage())
            .judgedAt(LocalDateTime.now())
            .build();

        int updatedCount = submissionMapper.updateJudgeSuccess(submission);

        if (updatedCount == 0) {
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
    }

    private void markFailed(Long submissionId, String errorMessage) {
        submissionMapper.updateJudgeFailure(submissionId, errorMessage);
    }

    private int defaultIfNull(Integer value) {
        return value == null ? 0 : value;
    }

    private Integer max(Integer left, Integer right) {
        if (left == null) {
            return right;
        }

        if (right == null) {
            return left;
        }

        return Integer.max(left, right);
    }

    private String prependCaseOrder(TestCase testCase, String errorMessage) {
        if (errorMessage == null || errorMessage.isBlank()) {
            return testCase.getCaseOrder() + "번 숨은 테스트케이스에서 실행 오류가 발생했습니다.";
        }

        return testCase.getCaseOrder() + "번 숨은 테스트케이스: " + errorMessage;
    }
}
