package com.ssafy.passit.problem.model;

import com.ssafy.passit.common.type.ProblemStatus;
import com.ssafy.passit.common.type.ProblemType;
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
public class Problem {

    private Long problemId;
    private Long certId;
    private Long authorId;
    private ProblemType problemType;
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private String constraintText;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String sourceType;
    private ProblemStatus status;
    private Double lastValidationScore;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
