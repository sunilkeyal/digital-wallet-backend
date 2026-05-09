package com.digitalwallet.controller;

import com.digitalwallet.dto.InsuranceCardDto;
import com.digitalwallet.service.InsuranceCardService;
import com.digitalwallet.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InsuranceCardControllerUnitTest {

    @Mock
    private InsuranceCardService insuranceCardService;

    @Mock
    private UserService userService;

    @InjectMocks
    private InsuranceCardController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(converter)
                .build();
    }

    @Test
    void getAllInsuranceCards_returnsList() throws Exception {
        when(userService.getCurrentUserId()).thenReturn("user123");
        when(insuranceCardService.getByUserId("user123", "provider", "asc")).thenReturn(List.of());

        mockMvc.perform(get("/api/insurance-cards").param("size", "1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createInsuranceCard_returnsCreated() throws Exception {
        InsuranceCardDto dto = new InsuranceCardDto();
        dto.setId("card1");
        dto.setProvider("Aetna");

        when(userService.getCurrentUserId()).thenReturn("user123");
        when(insuranceCardService.createInsuranceCard(any())).thenReturn(dto);

        mockMvc.perform(post("/api/insurance-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"provider\":\"Aetna\"}"))
                .andExpect(status().isCreated());
    }
}
