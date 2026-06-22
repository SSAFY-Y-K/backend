package com.ssafy.passit.problem.mapper;

import com.ssafy.passit.problem.dto.response.CodingProblemListItemResponse;
import com.ssafy.passit.problem.model.CategoryCount;
import com.ssafy.passit.problem.model.CodingProblem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CodingProblemMapper {

    void insertCodingProblem(CodingProblem problem);

    CodingProblem findById(@Param("problemId") Long problemId);

    List<CodingProblem> findAll();

    int deleteCodingProblem(@Param("problemId") Long problemId);

    Long findCertificationProblemCount();

    List<CodingProblem> findRecentCodingProblems(long limit);

    List<CategoryCount> findCategoryAndCount();
}
