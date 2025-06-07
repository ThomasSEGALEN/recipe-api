package com.globalopencampus.recipeapi.repository;

import com.globalopencampus.recipeapi.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByRecipeId(Long recipeId, Pageable pageable);
    Page<Comment> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.recipe.id = :recipeId AND c.rating IS NOT NULL")
    Double findAverageRatingByRecipeId(@Param("recipeId") Long recipeId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.recipe.id = :recipeId")
    Long countByRecipeId(@Param("recipeId") Long recipeId);

    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
}