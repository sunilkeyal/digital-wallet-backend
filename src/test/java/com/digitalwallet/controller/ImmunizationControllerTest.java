package com.digitalwallet.controller;

import com.digitalwallet.dto.ImmunizationDto;
import com.digitalwallet.service.ImmunizationService;
import com.digitalwallet.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImmunizationController.class)
class ImmunizationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImmunizationService immunizationService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void getAllImmunizations_returnsListOfImmunizations() throws Exception {
        when(userService.getCurrentUserId()).thenReturn("user123");
        when(immunizationService.getByUserId("user123")).thenReturn(List.of());

        mockMvc.perform(get("/api/immunizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getImmunization_returnsImmunization() throws Exception {
        ImmunizationDto dto = new ImmunizationDto();
        dto.setId("imm1");
        dto.setVaccineName("Flu Shot");
        dto.setAdministrationDate(LocalDate.of(2024, 1, 15));

        when(immunizationService.getById("imm1")).thenReturn(dto);

        mockMvc.perform(get("/api/immunizations/imm1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("imm1"))
                .andExpect(jsonPath("$.vaccineName").value("Flu Shot"));
    }

    @Test
    @WithMockUser
    void createImmunization_createsAndReturnsImmunization() throws Exception {
        ImmunizationDto dto = new ImmunizationDto();
        dto.setId("imm1");
        dto.setVaccineName("COVID-19");
        dto.setAdministrationDate(LocalDate.of(2024, 3, 1));

        when(userService.getCurrentUserId()).thenReturn("user123");
        when(immunizationService.createImmunization(any())).thenReturn(dto);

        mockMvc.perform(post("/api/immunizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vaccineName\":\"COVID-19\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void deleteImmunization_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/immunizations/imm1"))
                .andExpect(status().isNoContent());
    }
}
