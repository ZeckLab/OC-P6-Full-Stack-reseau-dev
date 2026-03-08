package com.openclassrooms.mddapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.openclassrooms.mddapi.dto.request.LoginDTO;
import com.openclassrooms.mddapi.dto.request.RegisterDTO;
import com.openclassrooms.mddapi.dto.response.ApiResponseDTO;
import com.openclassrooms.mddapi.dto.response.AuthSuccessDTO;
import com.openclassrooms.mddapi.exception.UnauthorizedException;
import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.constants.SuccessMessages;

/**
 * REST controller responsible for handling authentication operations,
 * including user login and registration.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

    /**
     * Authenticates a user using their username and password.
     * If authentication succeeds, a signed JWT token is returned.
     *
     * @param loginDto the login credentials (username and password)
     * @return AuthSuccessDTO containing the generated JWT token
     * @throws UnauthorizedException if authentication fails due to invalid credentials
     */
    @PostMapping("/login")
    public ResponseEntity<AuthSuccessDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        log.info("POST /auth/login - Login attempt for identifier: {}", loginDto.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            String token = jwtService.generateToken(authentication.getName());
            log.info("POST /auth/login - Login successful for {}", authentication.getName());

            return ResponseEntity.ok(new AuthSuccessDTO(token));

        } catch (AuthenticationException e) {
            log.warn("POST /auth/login - Login failed for identifier: {}", loginDto.getUsername());
            throw new UnauthorizedException(ErrorMessages.INVALID_CREDENTIALS);
        }
    }

    /**
     * Registers a new user account.
     *
     * @param registerDto the registration payload containing email, username, and password
     * @return ApiResponseDTO indicating successful registration with HTTP 201 status
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody RegisterDTO registerDto) {
        log.info("POST /auth/register - Registration attempt for email: {} and username: {}", registerDto.getEmail(), registerDto.getUsername());

        authService.register(registerDto);
        log.info("POST /auth/register - User '{}' registered successfully", registerDto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO(SuccessMessages.USER_REGISTERED, HttpStatus.CREATED.value()));
    }
}
