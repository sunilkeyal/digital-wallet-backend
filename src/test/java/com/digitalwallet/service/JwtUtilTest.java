package com.digitalwallet.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "digital-wallet-secret-key-must-be-at-least-256-bits-long-for-hs256-encryption");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void generateToken_createsValidToken() {
        String token = jwtUtil.generateToken("user123", "test@example.com", Set.of("ROLE_USER"));

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void parseToken_returnsClaims() {
        String token = jwtUtil.generateToken("user123", "test@example.com", Set.of("ROLE_USER"));

        Claims claims = jwtUtil.parseToken(token);

        assertEquals("user123", claims.getSubject());
        assertEquals("test@example.com", claims.get("email"));
    }

    @Test
    void getUserId_returnsCorrectUserId() {
        String token = jwtUtil.generateToken("user123", "test@example.com", Set.of("ROLE_USER"));

        String userId = jwtUtil.getUserId(token);

        assertEquals("user123", userId);
    }

    @Test
    void getEmail_returnsCorrectEmail() {
        String token = jwtUtil.generateToken("user123", "test@example.com", Set.of("ROLE_USER"));

        String email = jwtUtil.getEmail(token);

        assertEquals("test@example.com", email);
    }

    @Test
    void getRoles_returnsCorrectRoles() {
        String token = jwtUtil.generateToken("user123", "test@example.com", Set.of("ROLE_USER", "ROLE_ADMIN"));

        Set<String> roles = jwtUtil.getRoles(token);

        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    @Test
    void isTokenValid_returnsTrueForValidToken() {
        String token = jwtUtil.generateToken("user123", "test@example.com", Set.of("ROLE_USER"));

        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void isTokenValid_returnsFalseForInvalidToken() {
        assertFalse(jwtUtil.isTokenValid("invalid-token"));
    }
}
