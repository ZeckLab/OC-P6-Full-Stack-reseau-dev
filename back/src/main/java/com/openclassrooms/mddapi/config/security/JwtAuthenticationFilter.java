package com.openclassrooms.mddapi.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.openclassrooms.mddapi.config.AuthEntryPointJwt;

import java.io.IOException;

/**
 * JWT Filter: validates RS256 signatures using NimbusJwtDecoder.
 * If valid, sets the authenticated user in the SecurityContext.
 * Automatically rejects tampered, expired, or improperly signed tokens.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final AuthEntryPointJwt authEntryPointJwt;

    public JwtAuthenticationFilter(JwtDecoder jwtDecoder, AuthEntryPointJwt authEntryPointJwt) {
        this.jwtDecoder = jwtDecoder;
        this.authEntryPointJwt = authEntryPointJwt;
    }

    /**
     * Extracts and validates the JWT from the Authorization header.
     *
     * - Reads the "Authorization" header and checks for a Bearer token
     * - Decodes and validates the JWT using the JwtDecoder
     * - Creates an Authentication object and stores it in the SecurityContext
     * - Delegates invalid token handling to the custom AuthenticationEntryPoint
     * - Continues the filter chain if no token is present or after successful authentication
     *
     * @throws IOException if writing the error response fails
     * @throws ServletException if the filter chain fails
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the JWT (remove "Bearer ")
            String token = authHeader.substring(7);

            try {
                Jwt jwt = jwtDecoder.decode(token);

                // Create authentication object with the subject as principal
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        jwt.getSubject(), null, null);

                // Attach request details
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Store authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (AuthenticationException ex) {
                // Clear context and delegate error handling
                SecurityContextHolder.clearContext();
                authEntryPointJwt.commence(request, response, new BadCredentialsException("INVALID_TOKEN", ex));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
