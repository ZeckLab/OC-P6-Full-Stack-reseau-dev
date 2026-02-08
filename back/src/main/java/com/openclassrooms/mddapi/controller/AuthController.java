package com.openclassrooms.mddapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.service.JwtService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.dto.ApiResponseDTO;
import com.openclassrooms.mddapi.dto.AuthSuccessDTO;
import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.constants.SuccessMessages;
import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
    * Login returns a signed JWT containing the user's username as subject.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDto) {
        log.info("Login attempt for email: {}", loginDto.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            String token = jwtService.generateToken(authentication.getName());
            log.info("Login successful for user: {}", authentication.getName());

            return ResponseEntity.ok(new AuthSuccessDTO(token));

        } catch (Exception e) {
            log.warn("Login failed — invalid credentials for email/username: {}", loginDto.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(ErrorMessages.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED.value()));

        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody RegisterDTO registerDto) {
        log.info("Registration attempt for email and username: {} {}", registerDto.getEmail(), registerDto.getUsername());

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            log.warn("Registration failed — email already exists: {}", registerDto.getEmail());
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(ErrorMessages.EMAIL_ALREADY_IN_USE, HttpStatus.BAD_REQUEST.value()));
        }

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            log.warn("Registration failed — username already exists: {}", registerDto.getUsername());
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(ErrorMessages.USERNAME_ALREADY_IN_USE, HttpStatus.BAD_REQUEST.value()));
        }

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setHashPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);
        log.info("New user registered with email {} and username {}", user.getEmail(), user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO(SuccessMessages.USER_REGISTERED, HttpStatus.CREATED.value()));
    }
}
