package com.ssafy.passit.report.mapper;

import com.ssafy.passit.report.model.ProblemReport;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProblemReportMapper {

    void insert(ProblemReport report);

    List<ProblemReport> findAll();

    List<ProblemReport> findAllByProblemId(@Param("problemId") Long problemId);

    int resolve(@Param("reportId") Long reportId);
}
