package com.globalopencampus.recipeapi.controller;

import com.globalopencampus.recipeapi.dto.CommentDto;
import com.globalopencampus.recipeapi.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/recipes/{recipeId}/comments")
@Tag(name = "Comments", description = "Comment management endpoints")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    @Operation(summary = "Get comments by recipe", description = "Retrieve paginated comments for a recipe")
    public ResponseEntity<Page<CommentDto>> getCommentsByRecipeId(
            @PathVariable Long recipeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDto> comments = commentService.getCommentsByRecipeId(recipeId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @SecurityRequirement(name = "BearerAuthentication")
    @Operation(summary = "Create comment", description = "Add a comment to a recipe")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long recipeId,
            @Valid @RequestBody CommentDto commentDto,
            Authentication auth) {
        CommentDto createdComment = commentService.createComment(recipeId, commentDto, auth);
        return ResponseEntity.ok(createdComment);
    }
}