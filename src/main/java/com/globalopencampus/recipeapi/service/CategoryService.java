package com.globalopencampus.recipeapi.service;

import com.globalopencampus.recipeapi.dto.CategoryDto;
import com.globalopencampus.recipeapi.entity.Category;
import com.globalopencampus.recipeapi.exception.BadRequestException;
import com.globalopencampus.recipeapi.exception.ResourceNotFoundException;
import com.globalopencampus.recipeapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAllOrderByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return convertToDto(category);
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new BadRequestException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category category = new Category(categoryDto.getName(), categoryDto.getDescription());
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getName().equals(categoryDto.getName()) &&
                categoryRepository.existsByName(categoryDto.getName())) {
            throw new BadRequestException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getRecipes().isEmpty()) {
            throw new BadRequestException("Cannot delete category that contains recipes");
        }

        categoryRepository.delete(category);
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setRecipeCount((long) category.getRecipes().size());
        return dto;
    }
}