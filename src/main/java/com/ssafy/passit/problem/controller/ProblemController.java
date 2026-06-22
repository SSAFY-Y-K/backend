package com.ssafy.passit.problem.controller;

import com.ssafy.passit.problem.dto.entity.ProblemEntity;
import com.ssafy.passit.problem.dto.request.AiCreateRequest;
import com.ssafy.passit.problem.dto.request.MultipleChoiceProblemCreateRequest;
import com.ssafy.passit.problem.dto.request.ShortAnswerProblemCreateRequest;
import com.ssafy.passit.problem.dto.response.MultipleChoiceProblemResponse;
import com.ssafy.passit.problem.dto.response.ProblemsResponse;
import com.ssafy.passit.problem.dto.response.ShortAnswerProblemResponse;
import com.ssafy.passit.problem.model.CategoryCount;
import com.ssafy.passit.problem.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(summary = "AI에게 객관식 문제 생성 요청", description = "AI가 객관식 문제 생성하는 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "객관식 문제 생성 성공")
    })
    public ResponseEntity<Void> createMultipleChoiceProblemFromAi(
            @RequestBody AiCreateRequest request
    ) {

        problemService.generateAndSaveMultipleChoiceProblemFromAi(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/create/short-answer/ai")
    @Operation(summary = "AI에게 주관식 문제 생성 요청", description = "AI가 주관식 문제 생성하는 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주관식 문제 생성 성공")
    })
    public ResponseEntity<Void> createShortAnswerProblemFromAi(
            @RequestBody AiCreateRequest request) {

        problemService.generateAndSaveShortAnswerProblemFromAi(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "문제 ID, 제목, 타입 조회", description = """
            cert_id 가 null이면 모든 자격증 조회, 아니면 해당 자격증만 조회
            problem_id 기반으로 페이징, null이면 첫 페이지 조회
            problem_id는 현재까지 조회한 문제 중 가장 작은 problemId임
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문제 조회 성공")
    })
    public ProblemsResponse findProblems(
            @RequestParam(value = "cert_id", required = false) Long certId,
            @RequestParam(value = "problem_id", required = false) Long problemId
    ) {
        return problemService.findProblems(certId, problemId);
    }

    @GetMapping("/multiple-choice/{problemId}")
    @Operation(summary = "객관식 문제 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문제 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문제")
    })
    public ResponseEntity<MultipleChoiceProblemResponse> findMultipleChoiceProblem(
            @PathVariable long problemId
    ) {
        var problem = problemService.findMultipleChoiceProblem(problemId);
        if (problem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(problem);
    }

    @GetMapping("/short-answer/{problemId}")
    @Operation(summary = "주관식 문제 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문제 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문제")
    })
    public ResponseEntity<ShortAnswerProblemResponse> findShortAnswerProblem(
            @PathVariable long problemId
    ) {
        var problem = problemService.findShortAnswerProblem(problemId);
        if (problem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(problem);
    }

    @GetMapping("/certification/count")
    @Operation(summary = "자격증 문제 개수 조회")
    public Long findCertificationProblemCount() {
        return problemService.findCertificationProblemCount();
    }

    @GetMapping("/certification/recent")
    @Operation(summary = "최근 등록된 자격증 문제 n개 조회")
    public List<ProblemEntity> findRecentCertificationProblems(
            @RequestParam() long limit
    ) {
        return problemService.findRecentCertificationProblems(limit);
    }
}
