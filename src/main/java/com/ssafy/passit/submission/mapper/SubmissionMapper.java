package com.ssafy.passit.submission.mapper;

import com.ssafy.passit.submission.dto.MySubmissionResponse;
import com.ssafy.passit.submission.model.Submission;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubmissionMapper {

    int insertSubmission(Submission submission);

    Submission findById(@Param("submissionId") Long submissionId);

    int updateStatusRunning(@Param("submissionId") Long submissionId);

    int updateJudgeSuccess(Submission submission);

    int updateJudgeFailure(
        @Param("submissionId") Long submissionId,
        @Param("errorMessage") String errorMessage
    );

    List<MySubmissionResponse> findByUserId(@Param("userId") Long userId);
}
