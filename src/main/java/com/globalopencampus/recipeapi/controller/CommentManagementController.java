package com.globalopencampus.recipeapi.controller;

import com.globalopencampus.recipeapi.dto.CommentDto;
import com.globalopencampus.recipeapi.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/comments")
@Tag(name = "Comments", description = "Comment management endpoints")
@SecurityRequirement(name = "BearerAuthentication")
public class CommentManagementController {

    @Autowired
    private CommentService commentService;

    @PutMapping("/{id}")
    @Operation(summary = "Update comment", description = "Update existing comment")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentDto commentDto,
            Authentication auth) {
        CommentDto updatedComment = commentService.updateComment(id, commentDto, auth);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete comment", description = "Delete comment")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, Authentication auth) {
        commentService.deleteComment(id, auth);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}