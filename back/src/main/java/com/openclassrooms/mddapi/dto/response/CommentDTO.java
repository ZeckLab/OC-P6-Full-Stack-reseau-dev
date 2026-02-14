package com.openclassrooms.mddapi.dto.response;

public record CommentDTO(
        Long id,
        String content,
        String authorUsername,
        String createdAt
) {}
