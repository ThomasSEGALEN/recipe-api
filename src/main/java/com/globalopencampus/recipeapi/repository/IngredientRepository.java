package com.globalopencampus.recipeapi.repository;

import com.globalopencampus.recipeapi.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByRecipeId(Long recipeId);

    @Query("SELECT DISTINCT i.name FROM Ingredient i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<String> findDistinctIngredientNames(@Param("name") String name);

    @Query("SELECT i FROM Ingredient i WHERE i.recipe.id = :recipeId ORDER BY i.name ASC")
    List<Ingredient> findByRecipeIdOrderByName(@Param("recipeId") Long recipeId);

    void deleteByRecipeId(Long recipeId);
}