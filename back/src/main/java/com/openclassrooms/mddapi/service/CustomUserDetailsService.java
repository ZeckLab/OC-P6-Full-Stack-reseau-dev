package com.openclassrooms.mddapi.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.constants.ErrorMessages;

/**
 * Custom implementation of {@link UserDetailsService} used by Spring Security.
 * <p>
 * Loads user details from the database using the provided email address or username.
 * This service is invoked during authentication to retrieve user credentials
 * and roles (USER here).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
    * Accepts either username or email as login identifier.
    * Always returns a UserDetails whose principal is the username.
    */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Try to find user by user first, then by email
        User user = userRepository.findByUsername(identifier)
            .orElseGet(() -> userRepository.findByEmail(identifier)
            .orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND + identifier)));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getHashPassword())
            .authorities("USER")
            .build();
    }
}

