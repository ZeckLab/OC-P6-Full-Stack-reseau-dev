package com.openclassrooms.mddapi.dto.response;

/**
 * DTO representing a topic along with the current user's subscription status.
 */
public record TopicWithSubscriptionDto(
        Long id,
        String name,
        String description,
        boolean subscribed
) {}