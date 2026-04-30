package com.digitalwallet.controller;

import com.digitalwallet.dto.InsuranceCardDto;
import com.digitalwallet.service.InsuranceCardService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InsuranceCardController.class)
class InsuranceCardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceCardService insuranceCardService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void getAllInsuranceCards_returnsListOfCards() throws Exception {
        when(userService.getCurrentUserId()).thenReturn("user123");
        when(insuranceCardService.getByUserId("user123")).thenReturn(List.of());

        mockMvc.perform(get("/api/insurance-cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getInsuranceCard_returnsCard() throws Exception {
        InsuranceCardDto dto = new InsuranceCardDto();
        dto.setId("card1");
        dto.setProviderName("Blue Cross");
        dto.setPolicyNumber("POL-12345");

        when(insuranceCardService.getById("card1")).thenReturn(dto);

        mockMvc.perform(get("/api/insurance-cards/card1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("card1"))
                .andExpect(jsonPath("$.providerName").value("Blue Cross"));
    }

    @Test
    @WithMockUser
    void createInsuranceCard_createsAndReturnsCard() throws Exception {
        InsuranceCardDto dto = new InsuranceCardDto();
        dto.setId("card1");
        dto.setProviderName("Aetna");

        when(userService.getCurrentUserId()).thenReturn("user123");
        when(insuranceCardService.createInsuranceCard(any())).thenReturn(dto);

        mockMvc.perform(post("/api/insurance-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"providerName\":\"Aetna\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void deleteInsuranceCard_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/insurance-cards/card1"))
                .andExpect(status().isNoContent());
    }
}
