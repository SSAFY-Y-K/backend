package com.ssafy.passit.problem.service;

import com.ssafy.passit.client.ExternalServerClient;
import com.ssafy.passit.common.type.ProblemType;
import com.ssafy.passit.problem.dto.entity.MultipleChoiceProblemEntity;
import com.ssafy.passit.problem.dto.entity.ProblemEntity;
import com.ssafy.passit.problem.dto.entity.ShortAnswerProblemEntity;
import com.ssafy.passit.problem.dto.request.AiResponse;
import com.ssafy.passit.problem.dto.request.MultipleChoiceProblemCreateRequest;
import com.ssafy.passit.problem.dto.request.ShortAnswerProblemCreateRequest;
import com.ssafy.passit.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicProblemService implements ProblemService {

    private final ExternalServerClient client;
    private final ProblemRepository problemRepository;

    @Override
    @Transactional
    public void saveMultipleChoiceProblem(
            MultipleChoiceProblemCreateRequest request) {
        Long problemId = saveProblemAndGetId(ProblemEntity.builder()
                .certId(request.getCertId())
                .problemType(ProblemType.MULTIPLE_CHOICE)
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
    }

    @Override
    @Transactional
    public void saveShortChoiceProblem(ShortAnswerProblemCreateRequest request) {
        Long problemId = saveProblemAndGetId(ProblemEntity.builder()
                .certId(request.getCertId())
                .problemType(ProblemType.SHORT_ANSWER)
                .build());

        problemRepository.saveShortAnswerProblem(
                ShortAnswerProblemEntity.builder()
                        .problemId(problemId)
                        .question(request.getQuestion())
                        .answer(request.getAnswer())
                        .build());
    }

    @Override
    @Transactional
    public void generateAndSaveMultipleChoiceProblemFromAi(Long certId) {
        var createRequest = generateProblemFromAi(
                certId,
                ProblemType.MULTIPLE_CHOICE,
                MultipleChoiceProblemCreateRequest.class
        );
        createRequest.setCertId(certId);

        saveMultipleChoiceProblem(createRequest);
    }

    @Override
    @Transactional
    public void generateAndSaveShortAnswerProblemFromAi(Long certId) {
        var createRequest = generateProblemFromAi(
                certId,
                ProblemType.SHORT_ANSWER,
                ShortAnswerProblemCreateRequest.class
        );
        createRequest.setCertId(certId);

        saveShortChoiceProblem(createRequest);
    }

    private <T extends AiResponse> T generateProblemFromAi(
            Long certId,
            ProblemType problemType,
            Class<T> responseType) {
        String certificationName = findCertificationNameByCertId(certId);

        return client.generateProblem(
                certificationName,
                problemType,
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

    /**
     * certId로 자격증 이름 찾아 반환
     * @param certId
     * @return 자격증 이름
     */
    private String findCertificationNameByCertId(Long certId) {
        return problemRepository.findCertificationNameByCertId(certId);
    }
}
