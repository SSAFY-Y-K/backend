package com.ssafy.passit.submission.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.common.type.LanguageType;
import com.ssafy.passit.common.type.SubmissionStatus;
import com.ssafy.passit.judge.dto.JudgeResult;
import com.ssafy.passit.judge.service.JudgeService;
import com.ssafy.passit.problem.service.ProblemQueryService;
import com.ssafy.passit.submission.dto.CreateSubmissionRequest;
import com.ssafy.passit.submission.dto.SubmissionResultResponse;
import com.ssafy.passit.submission.mapper.SubmissionMapper;
import com.ssafy.passit.submission.model.Submission;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ProblemQueryService problemQueryService;
    private final SubmissionMapper submissionMapper;
    private final JudgeService judgeService;

    @Transactional
    public SubmissionResultResponse submit(Long problemId, CreateSubmissionRequest request) {
        validateSubmissionRequest(problemId, request);

        Submission submission = Submission.builder()
            .problemId(problemId)
            .userId(request.userId())
            .language(parseLanguage(request.language()))
            .sourceCode(request.sourceCode())
            .status(SubmissionStatus.PENDING)
            .build();

        submissionMapper.insertSubmission(submission);
        judgeService.judge(submission.getSubmissionId());

        return SubmissionResultResponse.from(findById(submission.getSubmissionId()));
    }

    public SubmissionResultResponse getSubmission(Long submissionId) {
        return SubmissionResultResponse.from(findById(submissionId));
    }

    public Submission findById(Long submissionId) {
        Submission submission = submissionMapper.findById(submissionId);

        if (submission == null) {
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }

        return submission;
    }

    @Transactional
    public void markRunning(Long submissionId) {
        int updatedCount = submissionMapper.updateStatusRunning(submissionId);

        if (updatedCount == 0) {
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
    }

    @Transactional
    public void markDone(JudgeResult result) {
        Submission submission = Submission.builder()
            .submissionId(result.submissionId())
            .status(result.status())
            .verdict(result.verdict())
            .execTimeMs(result.execTimeMs())
            .memoryKb(result.memoryKb())
            .errorMessage(result.errorMessage())
            .judgedAt(LocalDateTime.now())
            .build();

        int updatedCount = submissionMapper.updateJudgeSuccess(submission);

        if (updatedCount == 0) {
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
    }

    @Transactional
    public void markFailed(Long submissionId, String errorMessage) {
        int updatedCount = submissionMapper.updateJudgeFailure(submissionId, errorMessage);

        if (updatedCount == 0) {
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
    }

    private void validateSubmissionRequest(Long problemId, CreateSubmissionRequest request) {
        if (request == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "제출 요청 본문이 비어 있습니다.");
        }

        if (problemId == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "문제 ID는 필수입니다.");
        }

        if (request.userId() == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "사용자 ID는 필수입니다.");
        }

        if (request.sourceCode() == null || request.sourceCode().isBlank()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "소스코드는 비어 있을 수 없습니다.");
        }

        problemQueryService.getPublishedCodingProblem(problemId);
        parseLanguage(request.language());
    }

    private LanguageType parseLanguage(String language) {
        if (language == null || language.isBlank()) {
            throw new ApiException(ErrorCode.UNSUPPORTED_LANGUAGE, "언어 값은 필수입니다.");
        }

        try {
            return LanguageType.valueOf(language.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new ApiException(ErrorCode.UNSUPPORTED_LANGUAGE);
        }
    }
}
