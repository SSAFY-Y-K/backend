package com.ssafy.passit.judge.service;

import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.judge.dto.JudgeResult;
import com.ssafy.passit.judge.runner.CodeRunner;
import com.ssafy.passit.judge.support.OutputComparator;
import com.ssafy.passit.problem.service.ProblemQueryService;
import com.ssafy.passit.submission.mapper.SubmissionMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JudgeService {

    private final SubmissionMapper submissionMapper;
    private final ProblemQueryService problemQueryService;
    private final List<CodeRunner> codeRunners;
    private final OutputComparator outputComparator;

    public JudgeResult judge(Long submissionId) {
        throw new ApiException(ErrorCode.NOT_IMPLEMENTED, "채점 오케스트레이션 로직은 다음 단계에서 구현합니다.");
    }
}
