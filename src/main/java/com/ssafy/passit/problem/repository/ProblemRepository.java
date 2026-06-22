package com.ssafy.passit.problem.repository;

import com.ssafy.passit.problem.dto.entity.MultipleChoiceProblemEntity;
import com.ssafy.passit.problem.dto.entity.ProblemEntity;
import com.ssafy.passit.problem.dto.entity.ShortAnswerProblemEntity;
import com.ssafy.passit.problem.dto.response.MultipleChoiceProblemResponse;
import com.ssafy.passit.problem.dto.response.ShortAnswerProblemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemRepository {

    void saveProblem(ProblemEntity problem);

    void saveMultipleChoiceProblem(MultipleChoiceProblemEntity multipleChoiceProblem);

    void saveShortAnswerProblem(ShortAnswerProblemEntity shortAnswerProblem);

    List<ProblemEntity> findProblems(
            @Param("certId") Long certId,
            @Param("problemId") Long problemId,
            @Param("size") long size
    );


    MultipleChoiceProblemResponse findMultipleChoiceProblem(long problemId);

    ShortAnswerProblemResponse findShortAnswerProblem(long problemId);

    Long findCertificationProblemCount();

    List<ProblemEntity> findRecentCertificationProblems(long limit);
}
