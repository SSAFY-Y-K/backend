package com.ssafy.passit.problem.mapper;

import com.ssafy.passit.problem.model.TestCase;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestCaseMapper {

    List<TestCase> findHiddenTestCasesByProblemId(@Param("problemId") Long problemId);
}
