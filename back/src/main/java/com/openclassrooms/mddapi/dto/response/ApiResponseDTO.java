package com.openclassrooms.mddapi.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Generic API response wrapper containing a message, status code, and timestamp.
 */
@Getter
@RequiredArgsConstructor
public class ApiResponseDTO {
    private final String message;
    private final int status;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
