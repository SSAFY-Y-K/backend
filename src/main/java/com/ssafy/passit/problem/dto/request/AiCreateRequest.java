package com.ssafy.passit.problem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "AiCreateRequest",
        description = "Ai에게 문제 생성 요청 DTO"
)
public class AiCreateRequest {
    private Long certId;
    private @Nullable String referenceText;
}
