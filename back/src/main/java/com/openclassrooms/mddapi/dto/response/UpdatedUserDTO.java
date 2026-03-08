package com.openclassrooms.mddapi.dto.response;

/**
 * DTO returned after updating a user, optionally containing a new JWT token.
 */
public record UpdatedUserDTO(
        Long id,
        String email,
        String username,
        String token
) {}