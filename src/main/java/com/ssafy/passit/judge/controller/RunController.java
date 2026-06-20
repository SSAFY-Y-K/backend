package com.ssafy.passit.judge.controller;

import com.ssafy.passit.common.response.ApiResponse;
import com.ssafy.passit.judge.dto.RunCodeRequest;
import com.ssafy.passit.judge.dto.SampleRunResult;
import com.ssafy.passit.judge.service.CodeRunService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Run", description = "코드 실행 API (샘플 테스트케이스, 저장 없음)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RunController {

    private final CodeRunService codeRunService;

    @Operation(summary = "코드 실행", description = "샘플 테스트케이스로 코드를 실행합니다. 제출 기록은 저장되지 않습니다.")
    @PostMapping("/problems/{problemId}/run")
    public ApiResponse<List<SampleRunResult>> run(
        @PathVariable Long problemId,
        @RequestBody RunCodeRequest request
    ) {
        return ApiResponse.success(codeRunService.run(problemId, request));
    }
}
