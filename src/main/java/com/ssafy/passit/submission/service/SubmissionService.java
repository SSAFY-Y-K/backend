package com.ssafy.passit.submission.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.judge.dto.JudgeResult;
import com.ssafy.passit.judge.service.JudgeService;
import com.ssafy.passit.problem.service.ProblemQueryService;
import com.ssafy.passit.submission.dto.CreateSubmissionRequest;
import com.ssafy.passit.submission.dto.SubmissionResultResponse;
import com.ssafy.passit.submission.mapper.SubmissionMapper;
import com.ssafy.passit.submission.model.Submission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ProblemQueryService problemQueryService;
    private final SubmissionMapper submissionMapper;
    private final JudgeService judgeService;

    public SubmissionResultResponse submit(Long problemId, CreateSubmissionRequest request) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "제출 생성 로직은 다음 단계에서 구현합니다.");
    }

    public SubmissionResultResponse getSubmission(Long submissionId) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "제출 조회 로직은 다음 단계에서 구현합니다.");
    }

    public Submission findById(Long submissionId) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "제출 엔티티 조회 로직은 다음 단계에서 구현합니다.");
    }

    public void markRunning(Long submissionId) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "제출 상태 변경 로직은 다음 단계에서 구현합니다.");
    }

    public void markDone(JudgeResult result) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "채점 완료 저장 로직은 다음 단계에서 구현합니다.");
    }

    public void markFailed(Long submissionId, String errorMessage) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "채점 실패 저장 로직은 다음 단계에서 구현합니다.");
    }
}
