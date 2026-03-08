package com.openclassrooms.mddapi.dto.request;

import com.openclassrooms.mddapi.constants.ErrorMessages;

import jakarta.validation.constraints.NotBlank;


/**
 * DTO used for creating a new comment on an article.
 */
public record CreateCommentDTO(
    @NotBlank(message = ErrorMessages.CONTENT_REQUIRED)
    String content
) {}
