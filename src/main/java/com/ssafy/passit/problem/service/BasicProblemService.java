package com.ssafy.passit.problem.service;

import com.ssafy.passit.certification.service.CertificationService;
import com.ssafy.passit.client.ExternalServerClient;
import com.ssafy.passit.common.type.ProblemType;
import com.ssafy.passit.problem.dto.entity.MultipleChoiceProblemEntity;
import com.ssafy.passit.problem.dto.entity.ProblemEntity;
import com.ssafy.passit.problem.dto.entity.ShortAnswerProblemEntity;
import com.ssafy.passit.problem.dto.request.AiCreateRequest;
import com.ssafy.passit.problem.dto.request.AiResponse;
import com.ssafy.passit.problem.dto.request.MultipleChoiceProblemCreateRequest;
import com.ssafy.passit.problem.dto.request.ShortAnswerProblemCreateRequest;
import com.ssafy.passit.problem.dto.response.MultipleChoiceProblemResponse;
import com.ssafy.passit.problem.dto.response.ProblemsResponse;
import com.ssafy.passit.problem.dto.response.ShortAnswerProblemResponse;
import com.ssafy.passit.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicProblemService implements ProblemService {

    private static final long PAGE_SIZE = 9;

    private final ExternalServerClient client;
    private final ProblemRepository problemRepository;
    private final CertificationService certificationService;

    @Override
    @Transactional
    public void saveMultipleChoiceProblem(
            MultipleChoiceProblemCreateRequest request) {
        log.info("Saving multiple choice problem. certId={}, title={}",
                request.getCertId(), request.getTitle());
        Long problemId = saveProblemAndGetId(ProblemEntity.builder()
                .certId(request.getCertId())
                .problemType(ProblemType.MULTIPLE_CHOICE)
                .problemTitle(request.getTitle())
                .build());

        problemRepository.saveMultipleChoiceProblem(
                MultipleChoiceProblemEntity.builder()
                        .problemId(problemId)
                        .question(request.getQuestion())
                        .choice1Content(request.getChoice1Content())
                        .choice2Content(request.getChoice2Content())
                        .choice3Content(request.getChoice3Content())
                        .choice4Content(request.getChoice4Content())
                        .answerNumber(request.getAnswerNumber())
                        .build());
        log.info("Multiple choice problem saved. problemId={}, certId={}",
                problemId, request.getCertId());
    }

    @Override
    @Transactional
    public void saveShortChoiceProblem(ShortAnswerProblemCreateRequest request) {
        log.info("Saving short answer problem. certId={}, title={}",
                request.getCertId(), request.getTitle());
        Long problemId = saveProblemAndGetId(ProblemEntity.builder()
                .certId(request.getCertId())
                .problemType(ProblemType.SHORT_ANSWER)
                .problemTitle(request.getTitle())
                .build());

        problemRepository.saveShortAnswerProblem(
                ShortAnswerProblemEntity.builder()
                        .problemId(problemId)
                        .question(request.getQuestion())
                        .answer(request.getAnswer())
                        .build());
        log.info("Short answer problem saved. problemId={}, certId={}",
                problemId, request.getCertId());
    }

    @Override
    @Transactional
    public void generateAndSaveMultipleChoiceProblemFromAi(AiCreateRequest request) {
        log.info("AI multiple choice problem generation requested. certId={}", request.getCertId());
        var createRequest = generateProblemFromAi(
                request.getCertId(),
                ProblemType.MULTIPLE_CHOICE,
                request.getReferenceText(),
                MultipleChoiceProblemCreateRequest.class
        );
        createRequest.setCertId(request.getCertId());

        saveMultipleChoiceProblem(createRequest);
    }

    @Override
    @Transactional
    public void generateAndSaveShortAnswerProblemFromAi(AiCreateRequest request) {
        log.info("AI short answer problem generation requested. certId={}", request.getCertId());
        var createRequest = generateProblemFromAi(
                request.getCertId(),
                ProblemType.SHORT_ANSWER,
                request.getReferenceText(),
                ShortAnswerProblemCreateRequest.class
        );
        createRequest.setCertId(request.getCertId());

        saveShortChoiceProblem(createRequest);
    }

    @Override
    public ProblemsResponse findProblems(@Nullable Long certId, @Nullable Long problemId) {
        var problems = problemRepository.findProblems(certId, problemId, PAGE_SIZE);
        boolean isLast = problems.size() < PAGE_SIZE;

        return ProblemsResponse.builder()
                .problems(problems)
                .isLast(isLast)
                .build();
    }

    @Override
    public MultipleChoiceProblemResponse findMultipleChoiceProblem(long problemId) {
        return problemRepository.findMultipleChoiceProblem(problemId);
    }

    @Override
    public ShortAnswerProblemResponse findShortAnswerProblem(long problemId) {
        return problemRepository.findShortAnswerProblem(problemId);
    }

    private <T extends AiResponse> T generateProblemFromAi(
            Long certId,
            ProblemType problemType,
            String referenceText,
            Class<T> responseType) {
        String certificationName = certificationService.findCertificationName(certId);
        log.info("Requesting AI problem. certId={}, certificationName={}, problemType={}, referenceText={}",
                certId, certificationName, problemType, referenceText);

        return client.generateProblem(
                certificationName,
                problemType,
                referenceText,
                responseType
        );
    }

    /**
     * DB에 Problem을 저장하고 ID 값을 가져옴
     * @param problemEntity
     * @return Auto Increment된 ID 값
     */
    private Long saveProblemAndGetId(ProblemEntity problemEntity) {
        problemRepository.saveProblem(problemEntity);
        return problemEntity.getProblemId();
    }

}
