package com.globalopencampus.recipeapi.service;

import com.globalopencampus.recipeapi.dto.CommentDto;
import com.globalopencampus.recipeapi.entity.Comment;
import com.globalopencampus.recipeapi.entity.Recipe;
import com.globalopencampus.recipeapi.entity.Role;
import com.globalopencampus.recipeapi.entity.User;
import com.globalopencampus.recipeapi.exception.ResourceNotFoundException;
import com.globalopencampus.recipeapi.exception.UnauthorizedException;
import com.globalopencampus.recipeapi.repository.CommentRepository;
import com.globalopencampus.recipeapi.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public Page<CommentDto> getCommentsByRecipeId(Long recipeId, Pageable pageable) {
        return commentRepository.findByRecipeId(recipeId, pageable)
                .map(this::convertToDto);
    }

    public CommentDto createComment(Long recipeId, CommentDto commentDto, Authentication auth) {
        User user = getCurrentUser(auth);
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId));

        Comment comment = new Comment(commentDto.getContent(), commentDto.getRating(), user, recipe);
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    public CommentDto updateComment(Long commentId, CommentDto commentDto, Authentication auth) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        User user = getCurrentUser(auth);
        if (!comment.getUser().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("You don't have permission to update this comment");
        }

        comment.setContent(commentDto.getContent());
        comment.setRating(commentDto.getRating());

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    public void deleteComment(Long commentId, Authentication auth) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        User user = getCurrentUser(auth);
        if (!comment.getUser().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("You don't have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setRating(comment.getRating());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setRecipeId(comment.getRecipe().getId());
        dto.setRecipeTitle(comment.getRecipe().getTitle());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    private User getCurrentUser(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        return (User) auth.getPrincipal();
    }
}