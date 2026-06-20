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
public class CodingProblem {

    private Long problemId;
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private String constraintText;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String difficulty;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer totalSubmissions;
    private Integer acCount;
}
