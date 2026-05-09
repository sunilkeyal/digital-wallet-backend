package com.digitalwallet.controller;

import com.digitalwallet.dto.LabResultDto;
import com.digitalwallet.service.LabResultService;
import com.digitalwallet.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LabResultControllerTest {

    @Mock
    private LabResultService labResultService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LabResultController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(converter)
                .build();
    }

    @Test
    void getAllLabResults_returnsList() throws Exception {
        when(userService.getCurrentUserId()).thenReturn("user123");
        when(labResultService.getByUserId("user123", "testDate", "desc")).thenReturn(List.of());

        mockMvc.perform(get("/api/lab-results").param("size", "1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createLabResult_returnsCreated() throws Exception {
        LabResultDto dto = new LabResultDto();
        dto.setId("lab1");
        dto.setTestName("Blood Glucose");
        dto.setTestDate(LocalDate.of(2024, 4, 1));

        when(userService.getCurrentUserId()).thenReturn("user123");
        when(labResultService.createLabResult(any())).thenReturn(dto);

        mockMvc.perform(post("/api/lab-results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("lab1"));
    }

    @Test
    void updateLabResult_returnsOk() throws Exception {
        LabResultDto dto = new LabResultDto();
        dto.setId("lab1");
        dto.setTestName("Updated Test");
        dto.setTestDate(LocalDate.of(2024, 4, 1));

        when(labResultService.updateLabResult(eq("lab1"), any(LabResultDto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/lab-results/lab1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.testName").value("Updated Test"));
    }

    @Test
    void deleteLabResult_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/lab-results/lab1"))
                .andExpect(status().isNoContent());
    }
}
