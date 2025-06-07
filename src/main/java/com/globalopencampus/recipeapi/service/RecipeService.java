package com.globalopencampus.recipeapi.service;

import com.globalopencampus.recipeapi.dto.RecipeDto;
import com.globalopencampus.recipeapi.dto.IngredientDto;
import com.globalopencampus.recipeapi.entity.*;
import com.globalopencampus.recipeapi.exception.ResourceNotFoundException;
import com.globalopencampus.recipeapi.exception.UnauthorizedException;
import com.globalopencampus.recipeapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public Page<RecipeDto> getAllRecipes(Pageable pageable, Authentication auth) {
        Long currentUserId = getCurrentUserId(auth);
        return recipeRepository.findAllOrderByCreatedAtDesc(pageable)
                .map(recipe -> convertToDto(recipe, currentUserId));
    }

    public RecipeDto getRecipeById(Long id, Authentication auth) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));
        Long currentUserId = getCurrentUserId(auth);
        return convertToDto(recipe, currentUserId);
    }

    public Page<RecipeDto> getRecipesByUserId(Long userId, Pageable pageable, Authentication auth) {
        Long currentUserId = getCurrentUserId(auth);
        return recipeRepository.findByUserId(userId, pageable)
                .map(recipe -> convertToDto(recipe, currentUserId));
    }

    public Page<RecipeDto> getRecipesByCategory(Long categoryId, Pageable pageable, Authentication auth) {
        Long currentUserId = getCurrentUserId(auth);
        return recipeRepository.findByCategoryId(categoryId, pageable)
                .map(recipe -> convertToDto(recipe, currentUserId));
    }

    public Page<RecipeDto> searchRecipes(String keyword, Pageable pageable, Authentication auth) {
        Long currentUserId = getCurrentUserId(auth);
        return recipeRepository.findByKeyword(keyword, pageable)
                .map(recipe -> convertToDto(recipe, currentUserId));
    }

    @Transactional
    public RecipeDto createRecipe(RecipeDto recipeDto, Authentication auth) {
        User user = getCurrentUser(auth);
        Category category = categoryRepository.findById(recipeDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + recipeDto.getCategoryId()));

        Recipe recipe = new Recipe();
        updateRecipeFromDto(recipe, recipeDto);
        recipe.setUser(user);
        recipe.setCategory(category);

        Recipe savedRecipe = recipeRepository.save(recipe);

        // Save ingredients
        if (recipeDto.getIngredients() != null) {
            for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
                Ingredient ingredient = new Ingredient(ingredientDto.getName(),
                        ingredientDto.getQuantity(),
                        ingredientDto.getUnit());
                ingredient.setRecipe(savedRecipe);
                ingredientRepository.save(ingredient);
            }
        }

        return convertToDto(savedRecipe, user.getId());
    }

    @Transactional
    public RecipeDto updateRecipe(Long id, RecipeDto recipeDto, Authentication auth) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        User user = getCurrentUser(auth);
        if (!recipe.getUser().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("You don't have permission to update this recipe");
        }

        updateRecipeFromDto(recipe, recipeDto);

        if (recipeDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(recipeDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + recipeDto.getCategoryId()));
            recipe.setCategory(category);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        // Update ingredients
        if (recipeDto.getIngredients() != null) {
            ingredientRepository.deleteByRecipeId(id);
            for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
                Ingredient ingredient = new Ingredient(ingredientDto.getName(),
                        ingredientDto.getQuantity(),
                        ingredientDto.getUnit());
                ingredient.setRecipe(savedRecipe);
                ingredientRepository.save(ingredient);
            }
        }

        return convertToDto(savedRecipe, user.getId());
    }

    public void deleteRecipe(Long id, Authentication auth) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        User user = getCurrentUser(auth);
        if (!recipe.getUser().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("You don't have permission to delete this recipe");
        }

        recipeRepository.delete(recipe);
    }

    private void updateRecipeFromDto(Recipe recipe, RecipeDto dto) {
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        recipe.setInstructions(dto.getInstructions());
        recipe.setPreparationTime(dto.getPreparationTime());
        recipe.setCookingTime(dto.getCookingTime());
        recipe.setServings(dto.getServings());
        recipe.setDifficulty(dto.getDifficulty());
        recipe.setImageUrl(dto.getImageUrl());
    }

    private RecipeDto convertToDto(Recipe recipe, Long currentUserId) {
        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setInstructions(recipe.getInstructions());
        dto.setPreparationTime(recipe.getPreparationTime());
        dto.setCookingTime(recipe.getCookingTime());
        dto.setServings(recipe.getServings());
        dto.setDifficulty(recipe.getDifficulty());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setUserId(recipe.getUser().getId());
        dto.setUsername(recipe.getUser().getUsername());
        dto.setCategoryId(recipe.getCategory().getId());
        dto.setCategoryName(recipe.getCategory().getName());
        dto.setCreatedAt(recipe.getCreatedAt());
        dto.setUpdatedAt(recipe.getUpdatedAt());

        // Set ingredients
        List<IngredientDto> ingredients = recipe.getIngredients().stream()
                .map(this::convertIngredientToDto)
                .collect(Collectors.toList());
        dto.setIngredients(ingredients);

        // Set ratings and counts
        dto.setAverageRating(commentRepository.findAverageRatingByRecipeId(recipe.getId()));
        dto.setCommentCount(commentRepository.countByRecipeId(recipe.getId()));
        dto.setFavoriteCount(favoriteRepository.countByRecipeId(recipe.getId()));

        // Check if current user has favorited this recipe
        if (currentUserId != null) {
            dto.setIsFavorite(favoriteRepository.existsByUserIdAndRecipeId(currentUserId, recipe.getId()));
        }

        return dto;
    }

    private IngredientDto convertIngredientToDto(Ingredient ingredient) {
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setQuantity(ingredient.getQuantity());
        dto.setUnit(ingredient.getUnit());
        return dto;
    }

    private User getCurrentUser(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        return (User) auth.getPrincipal();
    }

    private Long getCurrentUserId(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        return ((User) auth.getPrincipal()).getId();
    }
}