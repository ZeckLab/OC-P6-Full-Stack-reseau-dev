package com.openclassrooms.mddapi.dto.response;

public record TopicWithSubscriptionDto(
        Long id,
        String name,
        String description,
        boolean subscribed
) {}