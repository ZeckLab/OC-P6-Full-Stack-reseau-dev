package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponseDTO {
    private final String message;
    private final int status;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
