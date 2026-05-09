package com.digitalwallet.controller;

import com.digitalwallet.dto.UserDto;
import com.digitalwallet.entity.User;
import com.digitalwallet.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void healthCheck_returnsOk() throws Exception {
        mockMvc.perform(get("/api/auth/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void login_returnsTokenAndUserId() throws Exception {
        User user = new User();
        user.setId("user123");
        user.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userService.validatePassword(user, "password123")).thenReturn(true);
        when(userService.generateToken(user)).thenReturn("token-abc");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", "test@example.com", "password", "password123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-abc"))
                .andExpect(jsonPath("$.userId").value("user123"));
    }

    @Test
    void oauth2Callback_createsNewUserWhenNotFound() throws Exception {
        User user = new User();
        user.setId("oauth-user");
        user.setEmail("oauth@example.com");
        user.setOauth2Provider("google");
        user.setOauth2Sub("sub-123");

        when(userService.findByOauth2ProviderAndSub("google", "sub-123")).thenReturn(Optional.empty());
        when(userService.createUser(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(user);
        when(userService.generateToken(user)).thenReturn("oauth-token");

        mockMvc.perform(post("/api/auth/oauth2/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "provider", "google",
                                "sub", "sub-123",
                                "email", "oauth@example.com",
                                "firstName", "Jane",
                                "lastName", "User"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("oauth-token"))
                .andExpect(jsonPath("$.userId").value("oauth-user"));

        verify(userService).createUser(org.mockito.ArgumentMatchers.any(User.class));
    }

    @Test
    void getCurrentUser_returnsUserDtoFromPrincipal() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId("current-user");
        userDto.setEmail("current@example.com");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("current-user", null);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        when(userService.getCurrentUserId()).thenReturn("current-user");
        when(userService.getUserById("current-user")).thenReturn(userDto);

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("current-user"))
                .andExpect(jsonPath("$.email").value("current@example.com"));
    }
}
