package com.globalopencampus.recipeapi.service;

import com.globalopencampus.recipeapi.dto.AuthResponseDto;
import com.globalopencampus.recipeapi.dto.LoginRequestDto;
import com.globalopencampus.recipeapi.dto.RegisterRequestDto;
import com.globalopencampus.recipeapi.entity.Role;
import com.globalopencampus.recipeapi.entity.User;
import com.globalopencampus.recipeapi.exception.BadRequestException;
import com.globalopencampus.recipeapi.repository.UserRepository;
import com.globalopencampus.recipeapi.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public AuthResponseDto authenticateUser(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = (User) authentication.getPrincipal();

        return new AuthResponseDto(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }

    public String registerUser(RegisterRequestDto signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setRole(Role.USER);
        userRepository.save(user);

        return "User registered successfully!";
    }
}