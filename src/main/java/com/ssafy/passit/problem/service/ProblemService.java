package com.ssafy.passit.problem.service;

import com.ssafy.passit.problem.dto.request.MultipleChoiceProblemCreateRequest;
import com.ssafy.passit.problem.dto.request.ShortAnswerProblemCreateRequest;

public interface ProblemService {

    /**
     * 사용자가 만든 객관식 문제를 DB에 저장
     * @param request 저장하려는 객관식 문제 요청 DTO
     */
    void saveMultipleChoiceProblem(MultipleChoiceProblemCreateRequest request);

    /**
     * 사용자가 만든 주관식 문제를 DB에 저장
     * @param request 저장하려는 주관식 문제 요청 DTO
     */
    void saveShortChoiceProblem(ShortAnswerProblemCreateRequest request);

    /**
     * AI에게 객관식 문제 생성 요청하고 저장
     * @param certId 생성할 문제의 자격증의 ID
     */
    void generateAndSaveMultipleChoiceProblemFromAi(Long certId);

    /**
     * AI에게 주관식 문제 생성 요청하고 저장
     * @param certId 생성할 문제의 자격증의 ID
     * @return
     */
    void generateAndSaveShortAnswerProblemFromAi(Long certId);
}
