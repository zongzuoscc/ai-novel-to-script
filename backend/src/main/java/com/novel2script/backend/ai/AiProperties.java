package com.novel2script.backend.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiProperties {

    private final String apiKey;

    private final String baseUrl;

    private final String modelId;

    public AiProperties(
            @Value("${AI_API_KEY:}") String apiKey,
            @Value("${AI_BASE_URL:https://api.openai.com/v1}") String baseUrl,
            @Value("${AI_MODEL_ID:gpt-4.1-mini}") String modelId
    ) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.modelId = modelId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getModelId() {
        return modelId;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }
}
