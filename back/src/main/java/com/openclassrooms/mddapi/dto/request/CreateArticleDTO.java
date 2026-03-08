package com.openclassrooms.mddapi.dto.request;

import com.openclassrooms.mddapi.constants.ErrorMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO used for creating a new article.
 */
@Data
public class CreateArticleDTO {
    @NotBlank(message = ErrorMessages.TITLE_REQUIRED)
    private String title;

    @NotBlank(message = ErrorMessages.CONTENT_REQUIRED)
    private String content;

    @NotNull(message = ErrorMessages.TOPIC_ID_REQUIRED)
    private Long topicId;
}