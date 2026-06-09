package com.ssafy.passit.problem.controller;

import com.ssafy.passit.problem.dto.ProblemCreateRequest;
import com.ssafy.passit.problem.dto.ProblemCreateFromAiRequest;
import com.ssafy.passit.problem.dto.ProblemSet;
import com.ssafy.passit.problem.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problem")
@Tag(name = "Problem API", description = "문제 생성 및 조회, 풀이 관련 API")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }


    @PostMapping("/create")
    @Operation(summary = "사용자가 문제 생성", description = "사용자가 직접 문제를 생성하는 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "문제 생성 성공")
    })
    public ResponseEntity<Void> createProblemsManual(@RequestBody ProblemCreateRequest request) {
        problemService.saveProblems(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "AI에게 문제 생성 요청", description = "AI에게 해당 자격증 문제 생성을 요청하는 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "문제 생성 성공")
    })
    @PostMapping("/create/ai")
    public ResponseEntity<Void> createProblemsFromAi(@RequestBody ProblemCreateFromAiRequest request) {
        problemService.createFromAi(request);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "문제 셋 조회", description = "문제 셋 조회 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문제 셋 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<ProblemSet>> getProblemSets(@RequestParam(value = "certId", required = false) Integer certId) {
        var problemSets = problemService.getProblemSets(certId);

        return ResponseEntity.ok().body(problemSets);
    }
}
