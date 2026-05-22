package com.ssafy.passit.problem.mapper;

import com.ssafy.passit.problem.model.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProblemMapper {

    Problem findPublishedCodingProblemById(@Param("problemId") Long problemId);
}
