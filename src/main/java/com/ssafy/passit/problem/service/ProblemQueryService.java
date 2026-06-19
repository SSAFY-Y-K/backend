package com.ssafy.passit.problem.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.problem.dto.response.CodingProblemDetailResponse;
import com.ssafy.passit.problem.dto.response.CodingProblemListItemResponse;
import com.ssafy.passit.problem.mapper.CodingProblemMapper;
import com.ssafy.passit.problem.mapper.TestCaseMapper;
import com.ssafy.passit.problem.model.CodingProblem;
import com.ssafy.passit.problem.model.TestCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemQueryService {

    private final CodingProblemMapper codingProblemMapper;
    private final TestCaseMapper testCaseMapper;

    public List<CodingProblemListItemResponse> findAllCodingProblems() {
        return codingProblemMapper.findAll().stream()
                .map(CodingProblemListItemResponse::from)
                .toList();
    }

    public CodingProblemDetailResponse findCodingProblemDetail(Long problemId) {
        CodingProblem problem = getCodingProblem(problemId);
        List<TestCase> sampleTestCases = testCaseMapper.findSampleTestCasesByProblemId(problemId);
        return CodingProblemDetailResponse.from(problem, sampleTestCases);
    }

    public void deleteCodingProblem(Long problemId) {
        getCodingProblem(problemId);
        codingProblemMapper.deleteCodingProblem(problemId);
    }

    public CodingProblem getCodingProblem(Long problemId) {
        CodingProblem problem = codingProblemMapper.findById(problemId);

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
