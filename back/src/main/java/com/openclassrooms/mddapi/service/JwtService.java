package com.openclassrooms.mddapi.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

/**
 * Service responsible for generating JWT tokens using asymmetric encryption (RS256).
 * Tokens include basic claims such as subject, issuer, issuance time, and expiration.
 */
@Service
public class JwtService {

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a signed JWT token using RS256, with the provided username as the subject.
     * <p>
     * This method does not rely on a full Authentication object since the application
     * does not implement role or permission management.
     *
     * @param username the username to include as the token subject
     * @return a signed JWT token string
     */
    public String generateToken(String username) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationMs))
                .issuer("mdd-api")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
