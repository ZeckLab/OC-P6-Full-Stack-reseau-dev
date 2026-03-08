package com.openclassrooms.mddapi.dto.response;

/**
 * DTO representing a summarized article without comments.
 */
public record ArticleDTO(
        Long id,
        String title,
        String content,
        String topicName,
        String authorUsername,
        String createdAt
) {}
