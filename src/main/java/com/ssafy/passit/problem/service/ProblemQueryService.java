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
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "문제 조회 로직은 다음 단계에서 구현합니다.");
    }

    public List<TestCase> getHiddenTestCases(Long problemId) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "숨은 테스트케이스 조회 로직은 다음 단계에서 구현합니다.");
    }
}
