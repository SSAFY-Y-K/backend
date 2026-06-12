package com.ssafy.passit.problem.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.problem.dto.GenerateAlgorithmRequest;
import com.ssafy.passit.problem.dto.GenerateAlgorithmResponse;
import com.ssafy.passit.problem.service.AlgorithmProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Algorithm Problems", description = "AI 알고리즘 코딩 문제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class AlgorithmProblemController {

    private final AlgorithmProblemService algorithmProblemService;

    @Operation(summary = "AI 코딩 문제 생성", description = "AI 서버를 통해 알고리즘 코딩 문제를 생성합니다. difficulty는 EASY / MEDIUM / HARD 중 하나입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "문제 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "AI 서버 오류")
    })
    @PostMapping("/algorithm/generate")
    public ApiResponse<GenerateAlgorithmResponse> generate(
            @RequestBody GenerateAlgorithmRequest request
    ) {
        return ApiResponse.success(algorithmProblemService.generate(request));
    }
}
