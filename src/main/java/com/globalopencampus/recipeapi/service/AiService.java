package com.globalopencampus.recipeapi.service;

import com.globalopencampus.recipeapi.dto.AiChatRequestDto;
import com.globalopencampus.recipeapi.dto.AiRecipeSuggestionRequestDto;
import com.globalopencampus.recipeapi.dto.AiResponseDto;
import com.globalopencampus.recipeapi.entity.Recipe;
import com.globalopencampus.recipeapi.repository.RecipeRepository;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AiService {

    @Autowired
    private MistralAiChatModel chatModel;

    @Autowired
    private RecipeRepository recipeRepository;

    public AiResponseDto generateRecipeSuggestion(AiRecipeSuggestionRequestDto request) {
        try {
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("You are a professional chef assistant. Create a detailed recipe suggestion using the following ingredients: ");
            promptBuilder.append(String.join(", ", request.getIngredients()));

            if (request.getCuisine() != null && !request.getCuisine().isEmpty()) {
                promptBuilder.append("\nCuisine style: ").append(request.getCuisine());
            }

            if (request.getDifficulty() != null && !request.getDifficulty().isEmpty()) {
                promptBuilder.append("\nDifficulty level: ").append(request.getDifficulty());
            }

            if (request.getMaxTime() != null) {
                promptBuilder.append("\nMaximum cooking time: ").append(request.getMaxTime()).append(" minutes");
            }

            promptBuilder.append("\n\nPlease provide:\n");
            promptBuilder.append("- Recipe title\n");
            promptBuilder.append("- Brief description\n");
            promptBuilder.append("- Complete ingredient list with quantities\n");
            promptBuilder.append("- Step-by-step cooking instructions\n");
            promptBuilder.append("- Preparation and cooking times\n");
            promptBuilder.append("- Number of servings\n");
            promptBuilder.append("- Any helpful tips or variations");

            String response = chatModel.call(promptBuilder.toString());
            return new AiResponseDto(response, "recipe_suggestion");
        } catch (Exception e) {
            return new AiResponseDto("Sorry, I'm unable to generate recipe suggestions at the moment. Please try again later.", "error");
        }
    }

    public AiResponseDto improveRecipe(Long recipeId) {
        try {
            Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
            if (recipeOpt.isEmpty()) {
                return new AiResponseDto("Recipe not found", "error");
            }

            Recipe recipe = recipeOpt.get();

            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("You are a professional chef consultant. Please analyze and provide improvement suggestions for this recipe:\n\n");
            promptBuilder.append("Title: ").append(recipe.getTitle()).append("\n");
            promptBuilder.append("Description: ").append(recipe.getDescription()).append("\n");
            promptBuilder.append("Instructions: ").append(recipe.getInstructions()).append("\n");
            promptBuilder.append("Preparation time: ").append(recipe.getPreparationTime()).append(" minutes\n");
            promptBuilder.append("Cooking time: ").append(recipe.getCookingTime()).append(" minutes\n");
            promptBuilder.append("Servings: ").append(recipe.getServings()).append("\n");
            promptBuilder.append("Difficulty: ").append(recipe.getDifficulty()).append("\n\n");

            promptBuilder.append("Please provide suggestions for:\n");
            promptBuilder.append("- Technique improvements\n");
            promptBuilder.append("- Flavor enhancements\n");
            promptBuilder.append("- Ingredient substitutions\n");
            promptBuilder.append("- Presentation tips\n");
            promptBuilder.append("- Time optimization\n");
            promptBuilder.append("- Common mistakes to avoid");

            String response = chatModel.call(promptBuilder.toString());
            return new AiResponseDto(response, "recipe_improvement");
        } catch (Exception e) {
            return new AiResponseDto("Sorry, I'm unable to analyze the recipe at the moment. Please try again later.", "error");
        }
    }

    public AiResponseDto chatWithAI(AiChatRequestDto request) {
        try {
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("You are a friendly and knowledgeable culinary assistant. ");
            promptBuilder.append("You help users with cooking questions, recipe suggestions, ");
            promptBuilder.append("ingredient substitutions, cooking techniques, and general food advice. ");

            if (request.getRecipeId() != null) {
                Optional<Recipe> recipeOpt = recipeRepository.findById(request.getRecipeId());
                if (recipeOpt.isPresent()) {
                    Recipe recipe = recipeOpt.get();
                    promptBuilder.append("The user is asking about this recipe: ");
                    promptBuilder.append(recipe.getTitle()).append(" - ").append(recipe.getDescription());
                    promptBuilder.append("\n\n");
                }
            }

            promptBuilder.append("User question: ").append(request.getMessage());
            promptBuilder.append("\n\nPlease provide a helpful, friendly, and informative response.");

            String response = chatModel.call(promptBuilder.toString());
            return new AiResponseDto(response, "chat");
        } catch (Exception e) {
            return new AiResponseDto("Sorry, I'm unable to respond at the moment. Please try again later.", "error");
        }
    }
}