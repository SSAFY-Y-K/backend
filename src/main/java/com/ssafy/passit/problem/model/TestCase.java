package com.ssafy.passit.problem.model;

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
public class TestCase {

    private Long testCaseId;
    private Long problemId;
    private String inputData;
    private String expectedOutput;
    private Boolean isSample;
    private Integer caseOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
