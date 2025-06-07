package com.globalopencampus.recipeapi.config;

import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${spring.ai.mistralai.api-key}")
    private String apiKey;

    @Bean
    public MistralAiApi mistralAiApi() {
        return new MistralAiApi(apiKey);
    }

    @Bean
    public MistralAiChatModel mistralAiChatModel(MistralAiApi mistralAiApi) {
        return new MistralAiChatModel(mistralAiApi);
    }
}