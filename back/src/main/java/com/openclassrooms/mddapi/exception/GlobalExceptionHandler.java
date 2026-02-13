package com.openclassrooms.mddapi.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.dto.response.ApiResponseDTO;

/**
 * Global exception handler for the application.
 * <p>
 * Captures and formats exceptions into standardized API responses.
 * Currently handles:
 * <ul>
 * <li>{@link UsernameNotFoundException} → returns 401 Unauthorized</li>
 * <li>{@link Exception} → returns 500 Internal Server Error</li>
 * <li>{@link MethodArgumentTypeMismatchException} → returns 400 Bad Request</li>
 * <li>{@link MethodArgumentNotValidException} → returns 400 Bad Request with validation error details</li>
 * <li>{@link BadRequestException} → returns 400 Bad Request with custom message</li>
 * <li>{@link NotFoundException} → returns 404 Not Found with custom message</li>
 * <li>{@link UnauthorizedException} → returns 401 Unauthorized with custom message</li>
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid parameter: " + ex.getName();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Invalid input");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseDTO> handleBadRequest(BadRequestException ex) {
        log.warn(ErrorMessages.BAD_REQUEST + "{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleNotFound(NotFoundException ex) {
        log.warn(ErrorMessages.NOT_FOUND + "{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseDTO> handleUnauthorized(UnauthorizedException ex) {
        log.warn(ErrorMessages.UNAUTHORIZED_ACCESS + "{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDTO(ex.getMessage(), HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleGenericException(Exception ex) {
        log.error(ErrorMessages.UNHANDLED_EXCEPTION + "{}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(ErrorMessages.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}