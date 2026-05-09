package com.digitalwallet.service;

import com.digitalwallet.dto.ImmunizationDto;
import com.digitalwallet.entity.Immunization;
import com.digitalwallet.repository.ImmunizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImmunizationServiceTest {

    @Mock
    private ImmunizationRepository immunizationRepository;

    @InjectMocks
    private ImmunizationService immunizationService;

    @Test
    void getByUserId_returnsDtoList() {
        Immunization entity = new Immunization();
        entity.setId("imm1");
        entity.setUserId("user1");
        entity.setVaccineName("Flu");

        when(immunizationRepository.findByUserId("user1", Sort.by(Sort.Direction.ASC, "vaccineName")))
                .thenReturn(List.of(entity));

        List<ImmunizationDto> result = immunizationService.getByUserId("user1", "vaccineName", "asc");

        assertEquals(1, result.size());
        assertEquals("imm1", result.get(0).getId());
        assertEquals("Flu", result.get(0).getVaccineName());
    }

    @Test
    void getByUserIdPaginated_returnsPageOfDtos() {
        Immunization entity = new Immunization();
        entity.setId("imm2");
        entity.setUserId("user1");
        entity.setVaccineName("COVID");

        Page<Immunization> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "administrationDate")), 1);
        when(immunizationRepository.findByUserId("user1", PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "administrationDate"))))
                .thenReturn(page);

        Page<ImmunizationDto> result = immunizationService.getByUserIdPaginated("user1", 0, 3, "administrationDate", "desc");

        assertEquals(1, result.getTotalElements());
        assertEquals("imm2", result.getContent().get(0).getId());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(immunizationRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> immunizationService.getById("missing"));
    }

    @Test
    void createImmunization_setsDatesAndReturnsDto() {
        ImmunizationDto dto = new ImmunizationDto();
        dto.setUserId("user2");
        dto.setVaccineName("MMR");
        dto.setAdministrationDate("2024-03-21");

        when(immunizationRepository.save(any(Immunization.class))).thenAnswer(invocation -> {
            Immunization saved = invocation.getArgument(0);
            saved.setId("imm-saved");
            return saved;
        });

        ImmunizationDto result = immunizationService.createImmunization(dto);

        assertEquals("imm-saved", result.getId());
        assertEquals("2024-03-21", result.getAdministrationDate());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void updateImmunization_invalidDate_returnsDtoWithNullDate() {
        Immunization existing = new Immunization();
        existing.setId("imm3");
        existing.setManufacturer("Old");

        ImmunizationDto dto = new ImmunizationDto();
        dto.setVaccineName("Updated Vaccine");
        dto.setManufacturer("Updated Manufacturer");
        dto.setAdministrationDate("invalid-date");

        when(immunizationRepository.findById("imm3")).thenReturn(Optional.of(existing));
        when(immunizationRepository.save(existing)).thenReturn(existing);

        ImmunizationDto result = immunizationService.updateImmunization("imm3", dto);

        assertEquals("Updated Vaccine", result.getVaccineName());
        assertNull(result.getAdministrationDate());
    }

    @Test
    void deleteImmunization_delegatesToRepository() {
        immunizationService.deleteImmunization("imm4");

        verify(immunizationRepository).deleteById("imm4");
    }
}
