package com.ssafy.passit.problem.controller;

import com.ssafy.passit.problem.dto.ProblemCreateRequest;
import com.ssafy.passit.problem.dto.ProblemCreateFromAiRequest;
import com.ssafy.passit.problem.service.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createProblemsManual(@RequestBody ProblemCreateRequest request) {
        problemService.saveProblems(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create/ai")
    public ResponseEntity<Void> createProblemsFromAi(@RequestBody ProblemCreateFromAiRequest request) {
        problemService.createFromAi(request);

        return ResponseEntity.ok().build();
    }
}
