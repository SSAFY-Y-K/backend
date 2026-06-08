package com.ssafy.passit.problem.repository;

import com.ssafy.passit.problem.dto.ProblemChoice;
import com.ssafy.passit.problem.dto.ProblemSet;
import com.ssafy.passit.problem.dto.SingleProblem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemRepository {
    void saveProblemSet(ProblemSet problemSet);

    void saveProblem(SingleProblem singleProblem);

    void saveProblemChoice(ProblemChoice problemChoice);

    String findCertificationByCertId(Integer certId);
}
