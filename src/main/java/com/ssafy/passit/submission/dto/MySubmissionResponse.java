package com.ssafy.passit.submission.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MySubmissionResponse {

    private Long submissionId;
    private Long problemId;
    private String problemTitle;
    private String language;
    private String verdict;
    private Integer execTimeMs;
    private LocalDateTime submittedAt;
}
