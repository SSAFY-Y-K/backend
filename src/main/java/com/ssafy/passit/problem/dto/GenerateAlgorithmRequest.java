package com.ssafy.passit.problem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GenerateAlgorithmRequest {

    private String difficulty = "MEDIUM";
    private String category = "구현";
}
