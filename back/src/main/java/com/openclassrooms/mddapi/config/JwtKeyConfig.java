package com.openclassrooms.mddapi.config;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.util.FileCopyUtils;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Loads RSA keys from the classpath and exposes JwtEncoder/JwtDecoder beans.
 * Uses Nimbus implementation required by Spring Security 6 for RS256 signing.
 */
@Slf4j
@Configuration
public class JwtKeyConfig {

    @Value("${jwt.private-key-path}")
    private String privateKeyPath;

    @Value("${jwt.public-key-path}")
    private String publicKeyPath;

    /**
    * Loads the RSA private key from the classpath.
    * Used by the JwtEncoder to sign JWT tokens (RS256).
    */
    private RSAPrivateKey loadPrivateKey() throws Exception {
        log.info("Loading RSA key from classpath: private={}", privateKeyPath);
        ClassPathResource resource = new ClassPathResource(privateKeyPath);
        byte[] keyBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    /**
    * Loads the RSA public key from the classpath.
    * Used by both the JwtEncoder and JwtDecoder for RS256 operations.
    */
    private RSAPublicKey loadPublicKey() throws Exception {
        log.info("Loading RSA key from classpath: public={}", publicKeyPath);
        ClassPathResource resource = new ClassPathResource(publicKeyPath);
        byte[] keyBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        RSAKey rsaKey = new RSAKey.Builder(loadPublicKey())
                .privateKey(loadPrivateKey())
                .build();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(loadPublicKey()).build();
    }
}
