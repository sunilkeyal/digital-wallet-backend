package com.digitalwallet.controller;

import com.digitalwallet.dto.ImmunizationDto;
import com.digitalwallet.service.ImmunizationService;
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
class ImmunizationControllerTest {

    @Mock
    private ImmunizationService immunizationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ImmunizationController controller;

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
    void getAllImmunizations_returnsList() throws Exception {
        when(userService.getCurrentUserId()).thenReturn("user123");
        when(immunizationService.getByUserId("user123", "vaccineName", "asc")).thenReturn(List.of());

        mockMvc.perform(get("/api/immunizations").param("size", "1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createImmunization_returnsCreated() throws Exception {
        ImmunizationDto dto = new ImmunizationDto();
        dto.setId("imm1");
        dto.setVaccineName("Flu Shot");

        when(userService.getCurrentUserId()).thenReturn("user123");
        when(immunizationService.createImmunization(any())).thenReturn(dto);

        mockMvc.perform(post("/api/immunizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("imm1"));
    }

    @Test
    void updateImmunization_returnsOk() throws Exception {
        ImmunizationDto dto = new ImmunizationDto();
        dto.setId("imm1");
        dto.setVaccineName("Updated Vaccine");

        when(immunizationService.updateImmunization(eq("imm1"), any(ImmunizationDto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/immunizations/imm1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vaccineName").value("Updated Vaccine"));
    }

    @Test
    void deleteImmunization_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/immunizations/imm1"))
                .andExpect(status().isNoContent());
    }
}
