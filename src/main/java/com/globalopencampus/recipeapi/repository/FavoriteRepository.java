package com.globalopencampus.recipeapi.repository;

import com.globalopencampus.recipeapi.entity.Favorite;
import com.globalopencampus.recipeapi.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserIdAndRecipeId(Long userId, Long recipeId);
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

    @Query("SELECT f.recipe FROM Favorite f WHERE f.user.id = :userId ORDER BY f.createdAt DESC")
    Page<Recipe> findFavoriteRecipesByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.recipe.id = :recipeId")
    Long countByRecipeId(@Param("recipeId") Long recipeId);

    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);
}
