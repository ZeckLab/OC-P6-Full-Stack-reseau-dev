package com.openclassrooms.mddapi.config;

import org.springframework.stereotype.Component;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.dto.response.ApiResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Custom authentication entry point for Spring Security.
 *
 * This component is triggered whenever a protected endpoint is accessed
 * with an invalid, expired, or missing JWT. Instead of returning the
 * default HTML error page, it sends a standardized JSON response with
 * an HTTP 401 status code. This ensures consistent error handling across
 * the API and allows the frontend to properly react to authentication failures.
 */

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        // Set JSON response type
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Return 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Write standardized error response body
        new ObjectMapper().writeValue(response.getOutputStream(),
            new ApiResponseDTO(ErrorMessages.INVALID_TOKEN, HttpStatus.UNAUTHORIZED.value()));
    }

}
