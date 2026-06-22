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
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

    private final ProblemQueryService problemQueryService;
    private final SubmissionMapper submissionMapper;
    private final JudgeService judgeService;

    @Transactional
    public SubmissionResultResponse submit(Long problemId, Long actorUserId, CreateSubmissionRequest request) {
        validateSubmissionRequest(problemId, request);
        LanguageType language = parseLanguage(request.language());
        log.info("Submission requested. problemId={}, actorUserId={}, language={}",
            problemId, actorUserId, language);

        Submission submission = Submission.builder()
            .problemId(problemId)
            .userId(actorUserId)
            .language(language)
            .sourceCode(request.sourceCode())
            .status(SubmissionStatus.PENDING)
            .build();

        submissionMapper.insertSubmission(submission);
        log.info("Submission saved. submissionId={}, problemId={}, userId={}",
            submission.getSubmissionId(), problemId, actorUserId);
        judgeService.judge(submission.getSubmissionId());

        return SubmissionResultResponse.from(findById(submission.getSubmissionId()));
    }

    public SubmissionResultResponse getSubmission(Long submissionId, Long actorUserId, boolean isAdmin) {
        Submission submission = findById(submissionId);
        validateOwnerOrAdmin(
            submission.getUserId(),
            actorUserId,
            isAdmin,
            "Only the owner or an admin can view this submission."
        );
        return SubmissionResultResponse.from(submission);
    }

    public Submission findById(Long submissionId) {
        Submission submission = submissionMapper.findById(submissionId);

        if (submission == null) {
            log.warn("Submission not found. submissionId={}", submissionId);
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }

        return submission;
    }

    @Transactional
    public void markRunning(Long submissionId) {
        int updatedCount = submissionMapper.updateStatusRunning(submissionId);

        if (updatedCount == 0) {
            log.warn("Failed to mark submission running. submissionId={}", submissionId);
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
        log.info("Submission marked running. submissionId={}", submissionId);
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
            log.warn("Failed to mark submission done. submissionId={}", result.submissionId());
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
        log.info("Submission marked done. submissionId={}, verdict={}, execTimeMs={}, memoryKb={}",
            result.submissionId(), result.verdict(), result.execTimeMs(), result.memoryKb());
    }

    @Transactional
    public void markFailed(Long submissionId, String errorMessage) {
        int updatedCount = submissionMapper.updateJudgeFailure(submissionId, errorMessage);

        if (updatedCount == 0) {
            log.warn("Failed to mark submission failed. submissionId={}", submissionId);
            throw new ApiException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
        log.warn("Submission marked failed. submissionId={}, message={}", submissionId, errorMessage);
    }

    private void validateSubmissionRequest(Long problemId, CreateSubmissionRequest request) {
        if (request == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "?쒖텧 ?붿껌 蹂몃Ц??鍮꾩뼱 ?덉뒿?덈떎.");
        }

        if (problemId == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "臾몄젣 ID???꾩닔?낅땲??");
        }

        if (request.sourceCode() == null || request.sourceCode().isBlank()) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "?뚯뒪肄붾뱶??鍮꾩뼱 ?덉쓣 ???놁뒿?덈떎.");
        }

        problemQueryService.getCodingProblem(problemId);
        parseLanguage(request.language());
    }

    private LanguageType parseLanguage(String language) {
        if (language == null || language.isBlank()) {
            throw new ApiException(ErrorCode.UNSUPPORTED_LANGUAGE, "?몄뼱 媛믪? ?꾩닔?낅땲??");
        }

        try {
            return LanguageType.valueOf(language.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new ApiException(ErrorCode.UNSUPPORTED_LANGUAGE);
        }
    }

    private void validateOwnerOrAdmin(
        Long ownerUserId,
        Long actorUserId,
        boolean isAdmin,
        String message
    ) {
        if (isAdmin) {
            return;
        }
        if (!Objects.equals(ownerUserId, actorUserId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, message);
        }
    }
}
