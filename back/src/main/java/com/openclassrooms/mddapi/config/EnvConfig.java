package com.openclassrooms.mddapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Loads environment variables from the local `.env` file into the Spring
 * context.
 * <p>
 * This configuration is essential for resolving placeholders like
 * ${SERVER_PORT}
 * in `application.properties` when running the application locally.
 * <p>
 * Do not delete this class unless environment variables are injected by
 * another mechanism
 * (e.g. system-level export, Docker, CI/CD pipeline).
 */
@Configuration(proxyBeanMethods = false)
@PropertySource("file:.env")
public class EnvConfig {

    private EnvConfig() {
        // prevent instantiation
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
