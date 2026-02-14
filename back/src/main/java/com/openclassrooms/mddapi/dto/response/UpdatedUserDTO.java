package com.openclassrooms.mddapi.dto.response;

public record UpdatedUserDTO(
        Long id,
        String email,
        String username,
        String token
) {}