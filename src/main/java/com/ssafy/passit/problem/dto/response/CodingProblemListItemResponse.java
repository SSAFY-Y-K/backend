package com.ssafy.passit.problem.dto.response;

import com.ssafy.passit.problem.model.CodingProblem;
import java.time.LocalDateTime;

public record CodingProblemListItemResponse(
        Long problemId,
        String title,
        String difficulty,
        String category,
        LocalDateTime createdAt
) {
    public static CodingProblemListItemResponse from(CodingProblem problem) {
        return new CodingProblemListItemResponse(
                problem.getProblemId(),
                problem.getTitle(),
                problem.getDifficulty(),
                problem.getCategory(),
                problem.getCreatedAt()
        );
    }
}
