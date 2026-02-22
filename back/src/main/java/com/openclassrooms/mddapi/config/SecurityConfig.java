package com.openclassrooms.mddapi.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.openclassrooms.mddapi.config.security.JwtAuthenticationFilter;

/**
 * Configures Spring Security for stateless JWT authentication.
 * All requests except /auth/login and /auth/register require a valid JWT.
 */
@Configuration
public class SecurityConfig {
    
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    /**
     * Configures the application's security filter chain.
     *
     * - Disables CSRF (API REST stateless)
     * - Enforces stateless session management
     * - Allows public access to authentication endpoints
     * - Requires authentication for all other routes
     * - Adds a custom JWT authentication filter before Spring Security's username/password filter
     * - Enables CORS with default configuration
     *
     * @param http the HttpSecurity builder
     * @param jwtDecoder the JWT decoder used by the authentication filter
     * @return the configured SecurityFilterChain
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // API REST → pas de CSRF
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // pas de session
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/login", "/auth/register").permitAll() // endpoints publics
                    .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtDecoder),
                UsernamePasswordAuthenticationFilter.class)
            .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    /**
     * Configures CORS settings for the application.
     * <p>
     * Allowed origins are loaded from the {@code cors.allowed-origins} property,
     * enabling flexibility across environments (e.g., dev, staging, prod).
     *
     * @return the configured {@link CorsConfigurationSource}
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toList();

        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

