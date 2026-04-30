package com.digitalwallet.service;

import com.digitalwallet.dto.ImmunizationDto;
import com.digitalwallet.entity.Immunization;
import com.digitalwallet.repository.ImmunizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImmunizationServiceTest {
    @Mock
    private ImmunizationRepository immunizationRepository;

    @InjectMocks
    private ImmunizationService immunizationService;

    private Immunization testImmunization;

    @BeforeEach
    void setUp() {
        testImmunization = new Immunization();
        testImmunization.setId("imm1");
        testImmunization.setUserId("user123");
        testImmunization.setVaccineName("Flu Shot");
        testImmunization.setManufacturer("Sanofi");
        testImmunization.setLotNumber("LOT-2024-001");
        testImmunization.setAdministrationDate(LocalDate.of(2024, 1, 15));
        testImmunization.setAdministeredBy("Dr. Smith");
        testImmunization.setFacilityName("City Clinic");
        testImmunization.setCreatedAt(LocalDateTime.now());
        testImmunization.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getByUserId_returnsListOfImmunizations() {
        when(immunizationRepository.findByUserId("user123")).thenReturn(List.of(testImmunization));

        List<ImmunizationDto> results = immunizationService.getByUserId("user123");

        assertEquals(1, results.size());
        assertEquals("Flu Shot", results.get(0).getVaccineName());
    }

    @Test
    void getById_returnsImmunization() {
        when(immunizationRepository.findById("imm1")).thenReturn(Optional.of(testImmunization));

        ImmunizationDto result = immunizationService.getById("imm1");

        assertEquals("imm1", result.getId());
        assertEquals("Flu Shot", result.getVaccineName());
    }

    @Test
    void createImmunization_savesAndReturnsImmunization() {
        ImmunizationDto dto = new ImmunizationDto();
        dto.setVaccineName("COVID-19");
        dto.setUserId("user123");

        when(immunizationRepository.save(any(Immunization.class))).thenReturn(testImmunization);

        ImmunizationDto result = immunizationService.createImmunization(dto);

        assertNotNull(result);
        verify(immunizationRepository).save(any(Immunization.class));
    }

    @Test
    void updateImmunization_updatesAndReturnsImmunization() {
        when(immunizationRepository.findById("imm1")).thenReturn(Optional.of(testImmunization));
        when(immunizationRepository.save(any(Immunization.class))).thenReturn(testImmunization);

        ImmunizationDto dto = new ImmunizationDto();
        dto.setVaccineName("Updated Vaccine");
        dto.setAdministrationDate(LocalDate.of(2024, 6, 1));

        ImmunizationDto result = immunizationService.updateImmunization("imm1", dto);

        verify(immunizationRepository).save(any(Immunization.class));
    }

    @Test
    void deleteImmunization_deletesImmunization() {
        immunizationService.deleteImmunization("imm1");

        verify(immunizationRepository).deleteById("imm1");
    }
}
