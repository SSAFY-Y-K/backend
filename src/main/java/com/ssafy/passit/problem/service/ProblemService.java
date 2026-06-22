package com.ssafy.passit.problem.service;

import com.ssafy.passit.problem.dto.entity.ProblemEntity;
import com.ssafy.passit.problem.dto.request.AiCreateRequest;
import com.ssafy.passit.problem.dto.request.MultipleChoiceProblemCreateRequest;
import com.ssafy.passit.problem.dto.request.ShortAnswerProblemCreateRequest;
import com.ssafy.passit.problem.dto.response.MultipleChoiceProblemResponse;
import com.ssafy.passit.problem.dto.response.ProblemsResponse;
import com.ssafy.passit.problem.dto.response.ShortAnswerProblemResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

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
     * @param request 문제 생성 DTO
     */
    void generateAndSaveMultipleChoiceProblemFromAi(AiCreateRequest request);

    /**
     * AI에게 주관식 문제 생성 요청하고 저장
     * @param request 문제 생성 DTO
     * @return
     */
    void generateAndSaveShortAnswerProblemFromAi(AiCreateRequest request);

    /**
     * 문제를 페이지 단위로 조회
     * @param certId 조회할 자격증 (null이면 모든 자격증에 대해 조회)
     * @param problemId 현재까지 조회한 페이지 중 가장 작은 problem_id (null이면 첫 페이지)
     * @return
     */
    ProblemsResponse findProblems(@Nullable Long certId, @Nullable Long problemId);

    /**
     * 해당 id의 객관식 문제 조회
     * @param problemId 조회할 문제 id
     * @return
     */
    MultipleChoiceProblemResponse findMultipleChoiceProblem(long problemId);

    /**
     * 해당 id의 주관식 문제 조회
     * @param problemId 조회할 문제 id
     * @return
     */
    ShortAnswerProblemResponse findShortAnswerProblem(long problemId);

    /**
     * 자격증 문제 개수 조회
     * @return
     */
    Long findCertificationProblemCount();

    /**
     * 최근에 등록된 n개 자격증 문제 조회
     * @return
     */
    List<ProblemEntity> findRecentCertificationProblems(long limit);
}
