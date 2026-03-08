package com.openclassrooms.mddapi.dto.response;

import java.util.List;

/**
 * DTO representing a detailed view of an article, including its comments.
 */
public record ArticleDetailDTO(
        Long id,
        String title,
        String content,
        String topicName,
        String authorUsername,
        String createdAt,
        List<CommentDTO> comments
) {}
