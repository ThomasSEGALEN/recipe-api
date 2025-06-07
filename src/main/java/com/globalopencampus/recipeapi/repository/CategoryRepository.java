package com.globalopencampus.recipeapi.repository;

import com.globalopencampus.recipeapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT c FROM Category c ORDER BY c.name ASC")
    List<Category> findAllOrderByName();

    @Query("SELECT c FROM Category c LEFT JOIN c.recipes r GROUP BY c ORDER BY COUNT(r) DESC")
    List<Category> findAllOrderByRecipeCount();
}