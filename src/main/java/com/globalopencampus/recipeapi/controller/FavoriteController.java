package com.globalopencampus.recipeapi.controller;

import com.globalopencampus.recipeapi.dto.RecipeDto;
import com.globalopencampus.recipeapi.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/favorites")
@Tag(name = "Favorites", description = "Favorite management endpoints")
@SecurityRequirement(name = "BearerAuthentication")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user favorites", description = "Retrieve paginated favorite recipes for a user")
    public ResponseEntity<Page<RecipeDto>> getFavoritesByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDto> favorites = favoriteService.getFavoritesByUserId(userId, pageable, auth);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/recipe/{recipeId}")
    @Operation(summary = "Add to favorites", description = "Add a recipe to user's favorites")
    public ResponseEntity<String> addToFavorites(@PathVariable Long recipeId, Authentication auth) {
        String message = favoriteService.addToFavorites(recipeId, auth);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/recipe/{recipeId}")
    @Operation(summary = "Remove from favorites", description = "Remove a recipe from user's favorites")
    public ResponseEntity<String> removeFromFavorites(@PathVariable Long recipeId, Authentication auth) {
        String message = favoriteService.removeFromFavorites(recipeId, auth);
        return ResponseEntity.ok(message);
    }
}
