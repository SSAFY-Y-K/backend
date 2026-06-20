package com.ssafy.passit.problem.mapper;

import com.ssafy.passit.problem.model.TestCase;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestCaseMapper {

    List<TestCase> findHiddenTestCasesByProblemId(@Param("problemId") Long problemId);

    List<TestCase> findSampleTestCasesByProblemId(@Param("problemId") Long problemId);

    List<TestCase> findAllByProblemId(@Param("problemId") Long problemId);

    void insertTestCases(@Param("list") List<TestCase> testCases);

    int update(@Param("testCaseId") Long testCaseId,
               @Param("inputData") String inputData,
               @Param("expectedOutput") String expectedOutput);
}
