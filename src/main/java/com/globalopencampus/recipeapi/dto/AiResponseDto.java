package com.globalopencampus.recipeapi.dto;

public class AiResponseDto {
    private String response;
    private String type;

    public AiResponseDto() {}

    public AiResponseDto(String response, String type) {
        this.response = response;
        this.type = type;
    }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}