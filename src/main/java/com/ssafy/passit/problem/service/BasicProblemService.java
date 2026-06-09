package com.ssafy.passit.problem.service;

import com.ssafy.passit.common.type.ProblemType;
import com.ssafy.passit.problem.client.ExternalServerClient;
import com.ssafy.passit.problem.dto.ProblemCreateFromAiRequest;
import com.ssafy.passit.problem.dto.ProblemCreateRequest;
import com.ssafy.passit.problem.dto.ProblemSet;
import com.ssafy.passit.problem.dto.SingleProblem;
import com.ssafy.passit.problem.repository.ProblemRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BasicProblemService implements ProblemService {

    private final ExternalServerClient client;
    private final ProblemRepository problemRepository;

    public BasicProblemService(ExternalServerClient client, ProblemRepository problemRepository) {
        this.client = client;
        this.problemRepository = problemRepository;
    }

    @Override
    @Transactional
    public void saveProblems(ProblemCreateRequest request) {
        ProblemSet problemSet = createProblemSet(request.getCertId(), request.getUserId());
        createProblems(request.getProblems(), problemSet.getProblemSetId());
    }

    @Override
    public void createFromAi(ProblemCreateFromAiRequest request) {
        String certification = problemRepository.findCertificationByCertId(request.getCertId());
        var problemCreateRequest = client.requestProblemCreate(certification, request.getProblemCount());

        ProblemSet problemSet = createProblemSet(request.getCertId(), request.getUserId());
        createProblems(problemCreateRequest.getProblems(), problemSet.getProblemSetId());
    }

    @Override
    public List<ProblemSet> getProblemSets(@Nullable Integer certId) {
        return problemRepository.findProblemSetByCertId(certId);
    }

    /**
     * 해당 certId와 userId를 가진 ProblemSet을 DB에 저장
     * @param certId
     * @param userId
     * @return
     */
    private ProblemSet createProblemSet(Integer certId, Integer userId) {
        ProblemSet problemSet = ProblemSet.builder()
                .certId(certId)
                .userId(userId)
                .build();

        problemRepository.saveProblemSet(problemSet);
        return problemSet;
    }

    /**
     * 인자로 받은 Problem들을 DB에 저장
     * @param problems
     * @param problemSetId
     */
    private void createProblems(List<SingleProblem> problems, Integer problemSetId) {

        for (var problem : problems) {
            problem.setProblemSetId(problemSetId);
            problemRepository.saveProblem(problem);

            // 객관식이면 각 선택지를 추가로 DB에 넣음
            if (problem.getProblemType() == ProblemType.MULTIPLE) {
                for (var problemChoice : problem.getProblemChoices()) {
                    problemChoice.setProblemId(problem.getProblemId());
                    problemRepository.saveProblemChoice(problemChoice);
                }
            }
        }
    }
}
