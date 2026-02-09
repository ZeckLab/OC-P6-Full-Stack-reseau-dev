package com.openclassrooms.mddapi.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import com.openclassrooms.mddapi.dto.ApiResponseDTO;
import com.openclassrooms.mddapi.constants.ErrorMessages;

/**
 * Global exception handler for the application.
 * <p>
 * Captures and formats exceptions into standardized API responses.
 * Currently handles:
 * <ul>
 * <li>{@link UsernameNotFoundException} → returns 401 Unauthorized</li>
 * <li>{@link Exception} → returns 500 Internal Server Error</li>
 * </ul>
 * <p>
 * Note: Although the frontend only checks for HTTP status codes (e.g. 401),
 * this class remains useful for logging and consistent error formatting.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleUserNotFound(UsernameNotFoundException ex) {
        log.warn(ErrorMessages.USER_NOT_FOUND + "{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDTO(ErrorMessages.USER_NOT_AUTHENTICATED, HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Invalid input");

        return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseDTO> handleBadRequest(BadRequestException ex) {
        log.warn(ErrorMessages.BAD_REQUEST + "{}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleGenericException(Exception ex) {
        log.error(ErrorMessages.UNHANDLED_EXCEPTION + "{}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(ErrorMessages.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}