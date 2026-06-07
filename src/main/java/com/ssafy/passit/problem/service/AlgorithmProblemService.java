package com.ssafy.passit.problem.service;

import com.ssafy.passit.problem.client.AiServerClient;
import com.ssafy.passit.problem.dto.AiProblemResponse;
import com.ssafy.passit.problem.dto.GenerateAlgorithmRequest;
import com.ssafy.passit.problem.dto.GenerateAlgorithmResponse;
import com.ssafy.passit.problem.mapper.CodingProblemMapper;
import com.ssafy.passit.problem.mapper.TestCaseMapper;
import com.ssafy.passit.problem.model.CodingProblem;
import com.ssafy.passit.problem.model.TestCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlgorithmProblemService {

    private final AiServerClient aiServerClient;
    private final CodingProblemMapper codingProblemMapper;
    private final TestCaseMapper testCaseMapper;

    @Transactional
    public GenerateAlgorithmResponse generate(GenerateAlgorithmRequest request) {
        AiProblemResponse aiResponse = aiServerClient.generateAlgorithmProblem(request);

        CodingProblem problem = CodingProblem.builder()
                .title(aiResponse.getTitle())
                .description(aiResponse.getDescription())
                .inputDescription(aiResponse.getInputDescription())
                .outputDescription(aiResponse.getOutputDescription())
                .constraintText(aiResponse.getConstraintText())
                .timeLimit(aiResponse.getTimeLimit())
                .memoryLimit(aiResponse.getMemoryLimit())
                .difficulty(request.getDifficulty())
                .category(aiResponse.getCategory())
                .build();

        codingProblemMapper.insertCodingProblem(problem);

        List<TestCase> testCases = aiResponse.getTestCases().stream()
                .map(tc -> TestCase.builder()
                        .problemId(problem.getProblemId())
                        .inputData(tc.getInputData())
                        .expectedOutput(tc.getExpectedOutput())
                        .isSample(tc.getIsSample())
                        .caseOrder(tc.getCaseOrder())
                        .build())
                .toList();

        testCaseMapper.insertTestCases(testCases);

        return new GenerateAlgorithmResponse(
                problem.getProblemId(),
                problem.getTitle(),
                problem.getCategory(),
                testCases.size()
        );
    }
}
