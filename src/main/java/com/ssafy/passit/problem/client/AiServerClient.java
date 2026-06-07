package com.ssafy.passit.problem.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.ssafy.passit.common.exception.ApiException;
import com.ssafy.passit.common.exception.ErrorCode;
import com.ssafy.passit.problem.dto.AiProblemResponse;
import com.ssafy.passit.problem.dto.GenerateAlgorithmRequest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiServerClient {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private final ObjectMapper snakeMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public AiProblemResponse generateAlgorithmProblem(GenerateAlgorithmRequest request) {
        try {
            byte[] body = snakeMapper.writeValueAsBytes(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(aiServerUrl + "/algorithm/generate"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() >= 400) {
                throw new ApiException(ErrorCode.AI_SERVER_ERROR,
                        "AI 서버 오류 [" + response.statusCode() + "]: " + response.body());
            }

            return snakeMapper.readValue(response.body(), AiProblemResponse.class);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.AI_SERVER_ERROR, e.getMessage());
        }
    }
}
