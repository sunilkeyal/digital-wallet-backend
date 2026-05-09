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
        ReflectionTestUtils.setField(jwtUtil, "secret", "super-secret-key-super-secret-key-super-secret-key");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L);
    }

    @Test
    void generateToken_containsExpectedClaims() {
        String token = jwtUtil.generateToken("user1", "user@example.com", Set.of("ROLE_USER"));

        assertNotNull(token);
        assertEquals("user1", jwtUtil.getUserId(token));
        assertEquals("user@example.com", jwtUtil.getEmail(token));
        assertTrue(jwtUtil.getRoles(token).contains("ROLE_USER"));
    }

    @Test
    void parseToken_returnsClaims() {
        String token = jwtUtil.generateToken("user2", "hello@example.com", Set.of("ROLE_ADMIN"));

        Claims claims = jwtUtil.parseToken(token);

        assertEquals("user2", claims.getSubject());
        assertEquals("hello@example.com", claims.get("email", String.class));
    }

    @Test
    void isTokenValid_returnsFalseForInvalidToken() {
        assertFalse(jwtUtil.isTokenValid("bad-token"));
    }
}
