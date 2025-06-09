package com.globalopencampus.recipeapi.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class AiRecipeSuggestionRequestDto {
    @NotEmpty(message = "Ingredients list cannot be empty")
    private List<String> ingredients;

    private String cuisine;
    private String difficulty;
    private Integer maxTime;

    public AiRecipeSuggestionRequestDto() {}

    public AiRecipeSuggestionRequestDto(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Integer getMaxTime() { return maxTime; }
    public void setMaxTime(Integer maxTime) { this.maxTime = maxTime; }
}