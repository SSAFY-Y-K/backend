package com.ssafy.passit.submission.model;

import com.ssafy.passit.common.type.LanguageType;
import com.ssafy.passit.common.type.SubmissionStatus;
import com.ssafy.passit.common.type.VerdictType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    private Long submissionId;
    private Long problemId;
    private Long userId;
    private LanguageType language;
    private String sourceCode;
    private SubmissionStatus status;
    private VerdictType verdict;
    private Integer execTimeMs;
    private Integer memoryKb;
    private String errorMessage;
    private LocalDateTime submittedAt;
    private LocalDateTime judgedAt;
}
