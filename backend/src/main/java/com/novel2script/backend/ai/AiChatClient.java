package com.novel2script.backend.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class AiChatClient {

    private final AiProperties aiProperties;

    private final ObjectMapper objectMapper;

    private final RestClient.Builder restClientBuilder;

    public AiChatClient(
            AiProperties aiProperties,
            ObjectMapper objectMapper,
            RestClient.Builder restClientBuilder
    ) {
        this.aiProperties = aiProperties;
        this.objectMapper = objectMapper;
        this.restClientBuilder = restClientBuilder;
    }

    public String completeJson(String systemPrompt, String userPrompt) {
        if (!aiProperties.isConfigured()) {
            throw new IllegalStateException("未配置 AI_API_KEY");
        }

        Map<String, Object> requestBody = Map.of(
                "model", aiProperties.getModelId(),
                "temperature", 0.2,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        String responseBody = restClientBuilder.build()
                .post()
                .uri(aiProperties.getBaseUrl() + "/chat/completions")
                .header("Authorization", "Bearer " + aiProperties.getApiKey())
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);

        return extractContent(responseBody);
    }

    private String extractContent(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root.path("choices").path(0).path("message").path("content").asText();
            if (content == null || content.isBlank()) {
                throw new IllegalStateException("AI 响应中没有 content 字段");
            }
            return stripJsonFence(content);
        } catch (Exception ex) {
            throw new IllegalStateException("解析 AI 响应失败", ex);
        }
    }

    private String stripJsonFence(String content) {
        String trimmed = content.trim();
        if (trimmed.startsWith("```")) {
            int firstLineBreak = trimmed.indexOf('\n');
            int lastFence = trimmed.lastIndexOf("```");
            if (firstLineBreak >= 0 && lastFence > firstLineBreak) {
                return trimmed.substring(firstLineBreak + 1, lastFence).trim();
            }
        }
        return trimmed;
    }
}
