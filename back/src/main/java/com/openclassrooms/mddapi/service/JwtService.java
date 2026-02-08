package com.openclassrooms.mddapi.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a JWT token using RS256 and the user's username as the subject.
     * <p>
     * This method does not rely on a full Authentication object,
     * as the application does not implement role or permission management.
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
