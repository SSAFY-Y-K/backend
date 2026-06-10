package com.ssafy.passit.client;

import com.ssafy.passit.common.type.ProblemType;
import com.ssafy.passit.problem.dto.request.AiResponse;
import com.ssafy.passit.problem.dto.request.MultipleChoiceProblemCreateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class ExternalServerClient {

    private final RestClient restClient;

    public ExternalServerClient(@Value("${ai.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * AI에게 요청을 문제 생성 요청을 보내 만들어진 문제를 받는 메서드
     * @param certificationName 만들 자격증의 이름
     * @param problemType 만들 문제 종류
     * @param responseType 응답의 클래스 타입
     * @return 생성한 문제
     * @param <T> 응답 DTO
     */
    public <T extends AiResponse> T generateProblem(
            String certificationName,
            ProblemType problemType,
            Class<T> responseType) {
        Map<String, Object> request = Map.of(
                "certification", certificationName,
                "problemType", problemType
                );

        return restClient.post()
                .uri("/questions/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(responseType);
    }
}
