package com.globalopencampus.recipeapi.controller;

import com.globalopencampus.recipeapi.dto.AuthResponseDto;
import com.globalopencampus.recipeapi.dto.LoginRequestDto;
import com.globalopencampus.recipeapi.dto.RegisterRequestDto;
import com.globalopencampus.recipeapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management endpoints")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<AuthResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        AuthResponseDto authResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequestDto signUpRequest) {
        String message = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(message);
    }
}