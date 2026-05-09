package com.digitalwallet.controller;

import com.digitalwallet.config.DataInitializer;
import com.digitalwallet.dto.UserDto;
import com.digitalwallet.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private DataInitializer dataInitializer;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllUsers_returnsUserList() throws Exception {
        UserDto dto = new UserDto();
        dto.setId("user1");
        dto.setEmail("user@example.com");
        when(userService.getAllUsers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dto))));
    }

    @Test
    void createUser_returnsCreated() throws Exception {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        when(userService.createUserFromDto(any(UserDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void deleteUser_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/admin/users/user1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser("user1");
    }

    @Test
    void seedSampleData_returnsBadRequestWhenUserIdMissing() throws Exception {
        mockMvc.perform(post("/api/admin/seed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void seedSampleData_callsDataInitializerWhenValid() throws Exception {
        mockMvc.perform(post("/api/admin/seed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("userId", "user1"))))
                .andExpect(status().isOk());

        verify(dataInitializer).seedForUser("user1");
    }
}
