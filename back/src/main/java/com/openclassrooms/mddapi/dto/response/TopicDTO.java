package com.openclassrooms.mddapi.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "" })
public record TopicDTO(
        Long id,
        String name,
        String description
) {}

