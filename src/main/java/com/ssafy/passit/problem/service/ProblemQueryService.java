package com.ssafy.passit.problem.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.problem.mapper.ProblemMapper;
import com.ssafy.passit.problem.mapper.TestCaseMapper;
import com.ssafy.passit.problem.model.Problem;
import com.ssafy.passit.problem.model.TestCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemQueryService {

    private final ProblemMapper problemMapper;
    private final TestCaseMapper testCaseMapper;

    public Problem getPublishedCodingProblem(Long problemId) {
        Problem problem = problemMapper.findPublishedCodingProblemById(problemId);

        if (problem == null) {
            throw new ApiException(ErrorCode.PROBLEM_NOT_FOUND);
        }

        return problem;
    }

    public List<TestCase> getHiddenTestCases(Long problemId) {
        List<TestCase> testCases = testCaseMapper.findHiddenTestCasesByProblemId(problemId);

        if (testCases == null || testCases.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "채점 가능한 숨은 테스트케이스가 없습니다.");
        }

        return testCases;
    }
}
