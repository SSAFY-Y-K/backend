package com.ssafy.passit.problem.service;

import com.ssafy.passit.problem.dto.ProblemCreateRequest;
import com.ssafy.passit.problem.dto.ProblemCreateFromAiRequest;

public interface ProblemService {

    /**
     * 사용자가 생성한 문제들을 DB에 저장
     * @param request
     */
    void saveProblems(ProblemCreateRequest request);

    /**
     * 외부 AI 서버에 문제 생성 요청
     * @return
     */
    void createFromAi(ProblemCreateFromAiRequest request);
}
