package com.openclassrooms.mddapi.dto.response;

public record ArticleDTO(
        Long id,
        String title,
        String content,
        String topicName,
        String authorUsername,
        String createdAt
) {}
