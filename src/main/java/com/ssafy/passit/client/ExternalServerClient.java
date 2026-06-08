package com.ssafy.passit.problem.client;

import com.ssafy.passit.problem.dto.ProblemCreateRequest;
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

    public ProblemCreateRequest requestProblemCreate(String certification, Integer problemCount) {
        Map<String, Object> request = Map.of(
                "certification", certification,
                "problemCount", problemCount
                );

        return restClient.post()
                .uri("/questions/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ProblemCreateRequest.class);
    }
}
