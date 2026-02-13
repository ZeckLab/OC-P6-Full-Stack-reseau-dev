package com.openclassrooms.mddapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.request.UpdateUserDTO;
import com.openclassrooms.mddapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.openclassrooms.mddapi.constants.ErrorMessages;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getCurrentUser(String username) {
        log.info("Retrieving user with username: {}", username);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException(ErrorMessages.USER_NOT_FOUND + username));
        log.info("User retrieved successfully: {}", user.getUsername());

        return user;
    }

    public User updateUser(String username, UpdateUserDTO updateUserDTO) {
        User user = getCurrentUser(username);

        // verify if the email is not null and no use before updating
        if(updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().equals(user.getEmail())) {
            if(userRepository.existsByEmail(updateUserDTO.getEmail())) {
                throw new BadRequestException(ErrorMessages.EMAIL_ALREADY_IN_USE + " " + updateUserDTO.getEmail());
            }
            user.setEmail(updateUserDTO.getEmail());
        }

        // verify if the username is not null and no use before updating
        if(updateUserDTO.getUsername() != null && !updateUserDTO.getUsername().equals(user.getUsername())) {
            if(userRepository.existsByUsername(updateUserDTO.getUsername())) {
                throw new BadRequestException(ErrorMessages.USERNAME_ALREADY_IN_USE + " " + updateUserDTO.getUsername());
            }
            user.setUsername(updateUserDTO.getUsername());
        }

        // verify if the password is not null and not empty before updating
        if(StringUtils.hasText(updateUserDTO.getPassword())) {
            user.setHashPassword(passwordEncoder.encode(updateUserDTO.getPassword()));

        }

        return userRepository.save(user);
    }
}
