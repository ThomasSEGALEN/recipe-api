package com.globalopencampus.recipeapi.controller;

import com.globalopencampus.recipeapi.dto.RecipeDto;
import com.globalopencampus.recipeapi.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/recipes")
@Tag(name = "Recipes", description = "Recipe management endpoints")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    @Operation(summary = "Get all recipes", description = "Retrieve paginated list of all recipes")
    public ResponseEntity<Page<RecipeDto>> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDto> recipes = recipeService.getAllRecipes(pageable, auth);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID", description = "Retrieve recipe details by ID")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long id, Authentication auth) {
        RecipeDto recipe = recipeService.getRecipeById(id, auth);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get recipes by user", description = "Retrieve recipes created by specific user")
    public ResponseEntity<Page<RecipeDto>> getRecipesByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDto> recipes = recipeService.getRecipesByUserId(userId, pageable, auth);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get recipes by category", description = "Retrieve recipes from specific category")
    public ResponseEntity<Page<RecipeDto>> getRecipesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDto> recipes = recipeService.getRecipesByCategory(categoryId, pageable, auth);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes", description = "Search recipes by keyword")
    public ResponseEntity<Page<RecipeDto>> searchRecipes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDto> recipes = recipeService.searchRecipes(keyword, pageable, auth);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping
    @SecurityRequirement(name = "BearerAuthentication")
    @Operation(summary = "Create recipe", description = "Create a new recipe")
    public ResponseEntity<RecipeDto> createRecipe(@Valid @RequestBody RecipeDto recipeDto, Authentication auth) {
        RecipeDto createdRecipe = recipeService.createRecipe(recipeDto, auth);
        return ResponseEntity.ok(createdRecipe);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "BearerAuthentication")
    @Operation(summary = "Update recipe", description = "Update existing recipe")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeDto recipeDto, Authentication auth) {
        RecipeDto updatedRecipe = recipeService.updateRecipe(id, recipeDto, auth);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "BearerAuthentication")
    @Operation(summary = "Delete recipe", description = "Delete recipe")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id, Authentication auth) {
        recipeService.deleteRecipe(id, auth);
        return ResponseEntity.ok("Recipe deleted successfully");
    }
}