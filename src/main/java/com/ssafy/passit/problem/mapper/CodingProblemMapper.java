package com.ssafy.passit.problem.mapper;

import com.ssafy.passit.problem.model.CodingProblem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CodingProblemMapper {

    void insertCodingProblem(CodingProblem problem);

    CodingProblem findById(@Param("problemId") Long problemId);
}
