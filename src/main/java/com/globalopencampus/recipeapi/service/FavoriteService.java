package com.globalopencampus.recipeapi.service;

import com.globalopencampus.recipeapi.dto.RecipeDto;
import com.globalopencampus.recipeapi.entity.Favorite;
import com.globalopencampus.recipeapi.entity.Recipe;
import com.globalopencampus.recipeapi.entity.User;
import com.globalopencampus.recipeapi.exception.BadRequestException;
import com.globalopencampus.recipeapi.exception.ResourceNotFoundException;
import com.globalopencampus.recipeapi.exception.UnauthorizedException;
import com.globalopencampus.recipeapi.repository.CommentRepository;
import com.globalopencampus.recipeapi.repository.FavoriteRepository;
import com.globalopencampus.recipeapi.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Page<RecipeDto> getFavoritesByUserId(Long userId, Pageable pageable, Authentication auth) {
        User currentUser = getCurrentUser(auth);
        return favoriteRepository.findFavoriteRecipesByUserId(userId, pageable)
                .map(recipe -> convertRecipeToDto(recipe, currentUser.getId()));
    }

    @Transactional
    public String addToFavorites(Long recipeId, Authentication auth) {
        User user = getCurrentUser(auth);
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId));

        if (favoriteRepository.existsByUserIdAndRecipeId(user.getId(), recipeId)) {
            throw new BadRequestException("Recipe is already in favorites");
        }

        Favorite favorite = new Favorite(user, recipe);
        favoriteRepository.save(favorite);
        return "Recipe added to favorites successfully";
    }

    @Transactional
    public String removeFromFavorites(Long recipeId, Authentication auth) {
        User user = getCurrentUser(auth);

        if (!favoriteRepository.existsByUserIdAndRecipeId(user.getId(), recipeId)) {
            throw new ResourceNotFoundException("Recipe not found in favorites");
        }

        favoriteRepository.deleteByUserIdAndRecipeId(user.getId(), recipeId);
        return "Recipe removed from favorites successfully";
    }

    private RecipeDto convertRecipeToDto(Recipe recipe, Long currentUserId) {
        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setUserId(recipe.getUser().getId());
        dto.setUsername(recipe.getUser().getUsername());
        dto.setCategoryId(recipe.getCategory().getId());
        dto.setCategoryName(recipe.getCategory().getName());
        dto.setDifficulty(recipe.getDifficulty());
        dto.setPreparationTime(recipe.getPreparationTime());
        dto.setCookingTime(recipe.getCookingTime());
        dto.setServings(recipe.getServings());
        dto.setCreatedAt(recipe.getCreatedAt());

        // Set ratings and counts
        dto.setAverageRating(commentRepository.findAverageRatingByRecipeId(recipe.getId()));
        dto.setCommentCount(commentRepository.countByRecipeId(recipe.getId()));
        dto.setFavoriteCount(favoriteRepository.countByRecipeId(recipe.getId()));
        dto.setIsFavorite(true); // Since this is from favorites list

        return dto;
    }

    private User getCurrentUser(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        return (User) auth.getPrincipal();
    }
}