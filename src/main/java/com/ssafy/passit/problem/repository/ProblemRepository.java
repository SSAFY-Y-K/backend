package com.ssafy.passit.problem.repository;

import com.ssafy.passit.problem.dto.entity.MultipleChoiceProblemEntity;
import com.ssafy.passit.problem.dto.entity.ProblemEntity;
import com.ssafy.passit.problem.dto.entity.ShortAnswerProblemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemRepository {

    void saveProblem(ProblemEntity problem);

    void saveMultipleChoiceProblem(MultipleChoiceProblemEntity multipleChoiceProblem);

    void saveShortAnswerProblem(ShortAnswerProblemEntity shortAnswerProblem);
}
