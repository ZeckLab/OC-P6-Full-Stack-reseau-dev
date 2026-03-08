package com.openclassrooms.mddapi.dto.response;

/**
 * DTO representing a comment associated with an article.
 */
public record CommentDTO(
        Long id,
        String content,
        String authorUsername,
        String createdAt
) {}
