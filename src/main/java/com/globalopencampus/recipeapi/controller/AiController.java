package com.globalopencampus.recipeapi.controller;

import com.globalopencampus.recipeapi.dto.AiChatRequestDto;
import com.globalopencampus.recipeapi.dto.AiRecipeSuggestionRequestDto;
import com.globalopencampus.recipeapi.dto.AiResponseDto;
import com.globalopencampus.recipeapi.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ai")
@Tag(name = "AI Features", description = "AI-powered recipe assistance endpoints")
@SecurityRequirement(name = "BearerAuthentication")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/suggest-recipe")
    @Operation(summary = "Get recipe suggestions", description = "Generate recipe suggestions based on available ingredients")
    public ResponseEntity<AiResponseDto> suggestRecipe(@Valid @RequestBody AiRecipeSuggestionRequestDto request) {
        AiResponseDto response = aiService.generateRecipeSuggestion(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/improve-recipe/{recipeId}")
    @Operation(summary = "Get recipe improvements", description = "Get AI suggestions to improve an existing recipe")
    public ResponseEntity<AiResponseDto> improveRecipe(@PathVariable Long recipeId) {
        AiResponseDto response = aiService.improveRecipe(recipeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat")
    @Operation(summary = "Chat with AI", description = "Have a conversation with the AI culinary assistant")
    public ResponseEntity<AiResponseDto> chatWithAI(@Valid @RequestBody AiChatRequestDto request) {
        AiResponseDto response = aiService.chatWithAI(request);
        return ResponseEntity.ok(response);
    }
}