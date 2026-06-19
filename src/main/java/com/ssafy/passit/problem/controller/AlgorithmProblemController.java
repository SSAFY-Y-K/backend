package com.ssafy.passit.problem.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.problem.dto.GenerateAlgorithmRequest;
import com.ssafy.passit.problem.dto.GenerateAlgorithmResponse;
import com.ssafy.passit.problem.dto.response.CodingProblemDetailResponse;
import com.ssafy.passit.problem.dto.response.CodingProblemListItemResponse;
import com.ssafy.passit.problem.service.AlgorithmProblemService;
import com.ssafy.passit.problem.service.ProblemQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Algorithm Problems", description = "AI 알고리즘 코딩 문제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class AlgorithmProblemController {

    private final AlgorithmProblemService algorithmProblemService;
    private final ProblemQueryService problemQueryService;

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

    @Operation(summary = "코딩 문제 목록 조회", description = "저장된 모든 코딩 문제 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/algorithm")
    public ApiResponse<List<CodingProblemListItemResponse>> findAllCodingProblems() {
        return ApiResponse.success(problemQueryService.findAllCodingProblems());
    }

    @Operation(summary = "코딩 문제 상세 조회", description = "코딩 문제 상세 내용과 샘플 테스트케이스를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "문제 없음")
    })
    @GetMapping("/algorithm/{problemId}")
    public ApiResponse<CodingProblemDetailResponse> findCodingProblem(
            @PathVariable Long problemId
    ) {
        return ApiResponse.success(problemQueryService.findCodingProblemDetail(problemId));
    }

    @Operation(summary = "코딩 문제 삭제 (관리자 전용)")
    @DeleteMapping("/algorithm/{problemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCodingProblem(@PathVariable Long problemId) {
        problemQueryService.deleteCodingProblem(problemId);
    }
}
