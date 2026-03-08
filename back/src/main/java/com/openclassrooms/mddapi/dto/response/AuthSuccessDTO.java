package com.openclassrooms.mddapi.dto.response;

/**
 * DTO returned after successful authentication, containing a JWT token.
 */
public record AuthSuccessDTO(String token) {}
