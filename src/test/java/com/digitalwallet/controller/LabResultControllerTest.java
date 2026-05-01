package com.digitalwallet.controller;

import com.digitalwallet.dto.LabResultDto;
import com.digitalwallet.security.JwtAuthenticationFilter;
import com.digitalwallet.security.JwtUtil;
import com.digitalwallet.service.LabResultService;
import com.digitalwallet.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LabResultController.class)
@AutoConfigureMockMvc(addFilters = false)
class LabResultControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabResultService labResultService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @WithMockUser
    void getAllLabResults_returnsListOfResults() throws Exception {
        when(userService.getCurrentUserId()).thenReturn("user123");
        when(labResultService.getByUserId("user123")).thenReturn(List.of());

        mockMvc.perform(get("/api/lab-results").param("size", "1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getAllLabResults_withPagination_returnsPage() throws Exception {
        when(userService.getCurrentUserId()).thenReturn("user123");
        org.springframework.data.domain.Page<LabResultDto> mockPage = 
            new org.springframework.data.domain.PageImpl<>(List.of());
        when(labResultService.getByUserIdPaginated("user123", 0, 10)).thenReturn(mockPage);

        mockMvc.perform(get("/api/lab-results").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser
    void getLabResult_returnsResult() throws Exception {
        LabResultDto dto = new LabResultDto();
        dto.setId("lab1");
        dto.setTestName("Complete Blood Count");
        dto.setTestDate(LocalDate.of(2024, 2, 10));

        when(labResultService.getById("lab1")).thenReturn(dto);

        mockMvc.perform(get("/api/lab-results/lab1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("lab1"))
                .andExpect(jsonPath("$.testName").value("Complete Blood Count"));
    }

    @Test
    @WithMockUser
    void createLabResult_createsAndReturnsResult() throws Exception {
        LabResultDto dto = new LabResultDto();
        dto.setId("lab1");
        dto.setTestName("Blood Glucose");

        when(userService.getCurrentUserId()).thenReturn("user123");
        when(labResultService.createLabResult(any())).thenReturn(dto);

        mockMvc.perform(post("/api/lab-results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"testName\":\"Blood Glucose\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void deleteLabResult_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/lab-results/lab1"))
                .andExpect(status().isNoContent());
    }
}
