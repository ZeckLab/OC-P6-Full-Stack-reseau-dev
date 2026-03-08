package com.openclassrooms.mddapi.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

/**
 * Service responsible for managing user-related operations,
 * including retrieving the authenticated user and updating user account information.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Retrieves the currently authenticated user based on the security context.
     *
     * @return the authenticated User entity
     * @throws BadRequestException if no authenticated user is found
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new BadRequestException(ErrorMessages.USER_NOT_AUTHENTICATED);
        }

        String username = authentication.getName(); // fonctionne pour UserDetails ET String

        log.info("getCurrentUser - Retrieving user with username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.USER_NOT_FOUND + username));
    }

    /**
     * Updates the authenticated user's account information.
     * Supports updating email, username, and password, with uniqueness checks where applicable.
     *
     * @param updateUserDTO the payload containing the fields to update
     * @return the updated User entity
     * @throws BadRequestException if the new email or username is already in use
     */
    public User updateUser(UpdateUserDTO updateUserDTO) {
        User user = getCurrentUser();
        String username = user.getUsername();
        log.info("updateUser - Updating user: {}", username);

        // verify if the email is not null and no use before updating
        if (updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().equals(user.getEmail())) {
            log.info("updateUser - Updating email for user '{}': {} -> {}", username, user.getEmail(),
                    updateUserDTO.getEmail());
            if (userRepository.existsByEmail(updateUserDTO.getEmail())) {
                throw new BadRequestException(ErrorMessages.EMAIL_ALREADY_IN_USE + " " + updateUserDTO.getEmail());
            }
            user.setEmail(updateUserDTO.getEmail());
        }

        // verify if the username is not null and no use before updating
        if (updateUserDTO.getUsername() != null && !updateUserDTO.getUsername().equals(user.getUsername())) {
            log.info("updateUser - Updating username for user '{}': {} -> {}", username, user.getUsername(),
                    updateUserDTO.getUsername());
            if (userRepository.existsByUsername(updateUserDTO.getUsername())) {
                throw new BadRequestException(
                        ErrorMessages.USERNAME_ALREADY_IN_USE + " " + updateUserDTO.getUsername());
            }
            user.setUsername(updateUserDTO.getUsername());
        }

        // verify if the password is not null and not empty before updating
        if (StringUtils.hasText(updateUserDTO.getPassword())) {
            log.info("updateUser - Updating password for user '{}'", username);
            user.setHashPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        return userRepository.save(user);
    }
}
