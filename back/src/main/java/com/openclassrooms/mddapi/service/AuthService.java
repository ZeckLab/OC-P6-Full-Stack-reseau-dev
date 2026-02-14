package com.openclassrooms.mddapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.dto.request.RegisterDTO;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void register(RegisterDTO registerDto) {

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            log.warn("register - Registration failed (email already exists): {}", registerDto.getEmail());
            throw new BadRequestException(ErrorMessages.EMAIL_ALREADY_IN_USE);
        }

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            log.warn("register - Registration failed (username already exists): {}", registerDto.getUsername());
            throw new BadRequestException(ErrorMessages.USERNAME_ALREADY_IN_USE);
        }

        User user = userMapper.toEntity(registerDto);
        user.setHashPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);
        log.info("register - User registered successfully: email={}, username={}", user.getEmail(), user.getUsername());
    }
}
