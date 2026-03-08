package com.openclassrooms.mddapi.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * DTO representing a topic without subscription information.
 */
@JsonPropertyOrder({ "id", "" })
public record TopicDTO(
        Long id,
        String name,
        String description
) {}

