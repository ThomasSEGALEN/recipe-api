package com.globalopencampus.recipeapi.dto;

import jakarta.validation.constraints.NotBlank;

public class AiChatRequestDto {
    @NotBlank(message = "Message is required")
    private String message;

    private Long recipeId;

    public AiChatRequestDto() {}

    public AiChatRequestDto(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }
}