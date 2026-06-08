package com.ssafy.passit.problem.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.problem.dto.GenerateAlgorithmRequest;
import com.ssafy.passit.problem.dto.GenerateAlgorithmResponse;
import com.ssafy.passit.problem.service.AlgorithmProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class AlgorithmProblemController {

    private final AlgorithmProblemService algorithmProblemService;

    @PostMapping("/algorithm/generate")
    public ApiResponse<GenerateAlgorithmResponse> generate(
            @RequestBody GenerateAlgorithmRequest request
    ) {
        return ApiResponse.success(algorithmProblemService.generate(request));
    }
}
