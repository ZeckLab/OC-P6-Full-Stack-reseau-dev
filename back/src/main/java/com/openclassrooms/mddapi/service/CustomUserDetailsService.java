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
 * Loads user details from the database using the provided email address or
 * username.
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
     * Loads a user for authentication using either a username or an email.
     *
     * The identifier is interpreted as:
     * - an email if it contains '@'
     * - a username otherwise (usernames cannot contain '@')
     *
     * This guarantees that login identifiers are unambiguous.
     * The returned UserDetails always uses the username as the principal.
     */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        boolean isEmail = identifier.contains("@");

        User user = (isEmail ? userRepository.findByEmail(identifier) : userRepository.findByUsername(identifier))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND + identifier));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getHashPassword())
                .authorities("USER")
                .build();
    }
}
