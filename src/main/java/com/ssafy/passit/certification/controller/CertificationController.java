package com.ssafy.passit.certification.controller;

import com.ssafy.passit.certification.dto.Certification;
import com.ssafy.passit.certification.dto.CertificationProblemCount;
import com.ssafy.passit.certification.service.CertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/certification")
@RequiredArgsConstructor
@Tag(name = "Certification API", description = "자격증 관련 API")
public class CertificationController {

    private final CertificationService certificationService;

    @Operation(summary = "모든 자격증 정보 조회", description = "모든 자격증의 정보 조회 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 자격증 조회 성공")
    })
    @GetMapping
    public List<Certification> findAllCertifications() {
        return certificationService.findAllCertifications();
    }

    @Operation(summary = "자격증별 문제 개수 조회", description = "각 자격증별 현재 등록된 문제 개수 구하는 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("/problem-count")
    public List<CertificationProblemCount> findAllCertificationProblemCount() {
        return certificationService.findAllCertificationProblemCount();
    }

    @Operation(summary = "등록된 자격증 총 개수 조회", description = "현재 DB에 저장된 자격증 개수를 구하는 엔드포인트")
    @GetMapping("/count")
    public Long findCertificationCount() {
        return certificationService.findCertificationCount();
    }
}
