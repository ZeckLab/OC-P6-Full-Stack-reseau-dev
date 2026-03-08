package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user attempts to access a protected resource
 * without valid authentication.
 * <p>
 * Results in a 401 Unauthorized response.
 */@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
public UnauthorizedException(String message) {
        super(message);
    }
}
