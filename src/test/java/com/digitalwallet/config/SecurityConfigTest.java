package com.digitalwallet.config;

import com.digitalwallet.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void passwordEncoder_returnsBCryptInstance() {
        SecurityConfig config = new SecurityConfig(new JwtAuthenticationFilter(null));

        assertNotNull(config.passwordEncoder());
        assertTrue(config.passwordEncoder().matches("password", config.passwordEncoder().encode("password")));
    }

    @Test
    void corsConfigurationSource_allowsLocalhost() {
        SecurityConfig config = new SecurityConfig(new JwtAuthenticationFilter(null));
        var source = config.corsConfigurationSource();
        CorsConfiguration corsConfiguration = source.getCorsConfiguration(null);

        assertNotNull(corsConfiguration);
        assertTrue(corsConfiguration.getAllowedOrigins().contains("http://localhost:5173"));
        assertTrue(corsConfiguration.getAllowedMethods().contains("GET"));
        assertTrue(corsConfiguration.getAllowedHeaders().contains("*"));
        assertTrue(corsConfiguration.getAllowCredentials());
    }
}
