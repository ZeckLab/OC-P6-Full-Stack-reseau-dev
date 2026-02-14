package com.openclassrooms.mddapi.dto.response;

import java.util.List;

public record ArticleDetailDTO(
        Long id,
        String title,
        String content,
        String topicName,
        String authorUsername,
        String createdAt,
        List<CommentDTO> comments
) {}
