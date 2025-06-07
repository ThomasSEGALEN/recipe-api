package com.globalopencampus.recipeapi.repository;

import com.globalopencampus.recipeapi.entity.Recipe;
import com.globalopencampus.recipeapi.entity.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findByUserId(Long userId, Pageable pageable);
    Page<Recipe> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Recipe> findByDifficulty(Difficulty difficulty, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Recipe> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :ingredient, '%'))")
    Page<Recipe> findByIngredient(@Param("ingredient") String ingredient, Pageable pageable);

    @Query("SELECT r FROM Recipe r ORDER BY r.createdAt DESC")
    Page<Recipe> findAllOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT r FROM Recipe r LEFT JOIN r.comments c GROUP BY r ORDER BY AVG(c.rating) DESC")
    Page<Recipe> findAllOrderByRatingDesc(Pageable pageable);

    @Query("SELECT COUNT(r) FROM Recipe r WHERE r.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
}