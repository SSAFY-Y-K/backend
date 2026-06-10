package com.ssafy.passit.problem.controller;

import com.ssafy.passit.problem.dto.request.MultipleChoiceProblemCreateRequest;
import com.ssafy.passit.problem.dto.request.ShortAnswerProblemCreateRequest;
import com.ssafy.passit.problem.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
@Tag(name = "Problem API", description = "문제 생성 및 조회, 풀이 관련 API")
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/create/multiple-choice")
    @Operation(summary = "사용자가 객관식 문제 생성", description = "사용자가 직접 객관식 문제를 생성하는 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "객관식 문제 생성 성공")
    })
    public ResponseEntity<Void> createMultipleChoiceProblem(
            @RequestBody MultipleChoiceProblemCreateRequest request) {
        problemService.saveMultipleChoiceProblem(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/create/short-answer")
    @Operation(summary = "사용자가 주관식 문제 생성", description = "사용자가 직접 주관식 문제 생성하는 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주관식 문제 생성 성공")
    })
    public ResponseEntity<Void> createShortAnswerProblem(
            @RequestBody ShortAnswerProblemCreateRequest request) {
        problemService.saveShortChoiceProblem(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/create/multiple-choice/ai")
    public ResponseEntity<Void> createMultipleChoiceProblemFromAi(
            @RequestBody Long certId) {

        problemService.generateAndSaveMultipleChoiceProblemFromAi(certId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/create/short-answer/ai")
    public ResponseEntity<Void> createShortAnswerProblemFromAi(
            @RequestBody Long certId) {

        problemService.generateAndSaveShortAnswerProblemFromAi(certId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
