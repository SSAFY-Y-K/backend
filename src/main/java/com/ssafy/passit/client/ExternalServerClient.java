package com.ssafy.passit.client;

import com.ssafy.passit.common.type.ProblemType;
import com.ssafy.passit.problem.dto.request.AiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Component
@Slf4j
public class ExternalServerClient {

    private final RestClient restClient;

    public ExternalServerClient(@Value("${ai.base-url}") String baseUrl) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
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
            String referenceText,
            Class<T> responseType) {

        AiRequest request = AiRequest.builder()
                        .certification(certificationName)
                        .problemType(problemType)
                        .referenceText(referenceText)
                        .build();

        log.info("Requesting AI problem generation. certificationName={}, problemType={}, referenceText={}",
                certificationName, problemType, referenceText);

        T response = restClient.post()
                .uri("/questions/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(responseType);
        log.info("AI problem generation completed. certificationName={}, problemType={}",
                certificationName, problemType);
        return response;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class AiRequest {
        private String certification;
        private ProblemType problemType;
        private String referenceText;
    }
}
