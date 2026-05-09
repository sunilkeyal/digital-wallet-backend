package com.digitalwallet.service;

import com.digitalwallet.dto.InsuranceCardDto;
import com.digitalwallet.entity.InsuranceCard;
import com.digitalwallet.repository.InsuranceCardRepository;
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
class InsuranceCardServiceTest {

    @Mock
    private InsuranceCardRepository insuranceCardRepository;

    @InjectMocks
    private InsuranceCardService insuranceCardService;

    @Test
    void getByUserId_returnsDtoList() {
        InsuranceCard entity = new InsuranceCard();
        entity.setId("card1");
        entity.setUserId("user1");
        entity.setProvider("United");

        when(insuranceCardRepository.findByUserId("user1", Sort.by(Sort.Direction.ASC, "provider")))
                .thenReturn(List.of(entity));

        List<InsuranceCardDto> result = insuranceCardService.getByUserId("user1", "provider", "asc");

        assertEquals(1, result.size());
        assertEquals("card1", result.get(0).getId());
        assertEquals("United", result.get(0).getProvider());
    }

    @Test
    void getByUserIdPaginated_returnsPageOfDtos() {
        InsuranceCard entity = new InsuranceCard();
        entity.setId("card2");
        entity.setUserId("user1");
        entity.setProvider("Aetna");

        Page<InsuranceCard> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "effectiveDate")), 1);
        when(insuranceCardRepository.findByUserId("user1", PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "effectiveDate"))))
                .thenReturn(page);

        Page<InsuranceCardDto> result = insuranceCardService.getByUserIdPaginated("user1", 0, 5, "effectiveDate", "desc");

        assertEquals(1, result.getTotalElements());
        assertEquals("card2", result.getContent().get(0).getId());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(insuranceCardRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> insuranceCardService.getById("missing"));
    }

    @Test
    void createInsuranceCard_setsTimestampsAndReturnsDto() {
        InsuranceCardDto dto = new InsuranceCardDto();
        dto.setUserId("user2");
        dto.setProvider("Cigna");

        when(insuranceCardRepository.save(any(InsuranceCard.class))).thenAnswer(invocation -> {
            InsuranceCard saved = invocation.getArgument(0);
            saved.setId("card-saved");
            return saved;
        });

        InsuranceCardDto result = insuranceCardService.createInsuranceCard(dto);

        assertEquals("card-saved", result.getId());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void updateInsuranceCard_updatesFieldsAndReturnsDto() {
        InsuranceCard existing = new InsuranceCard();
        existing.setId("card3");
        existing.setProvider("Old Provider");
        existing.setCreatedAt(LocalDateTime.of(2024, 1, 1, 0, 0));

        InsuranceCardDto dto = new InsuranceCardDto();
        dto.setProvider("New Provider");
        dto.setPolicyNumber("POL123");
        dto.setGroupNumber("GRP123");
        dto.setMemberName("John Doe");
        dto.setRelationship("Self");
        dto.setEffectiveDate(LocalDate.of(2024, 2, 1));
        dto.setExpiryDate(LocalDate.of(2025, 2, 1));

        when(insuranceCardRepository.findById("card3")).thenReturn(Optional.of(existing));
        when(insuranceCardRepository.save(existing)).thenReturn(existing);

        InsuranceCardDto result = insuranceCardService.updateInsuranceCard("card3", dto);

        assertEquals("New Provider", result.getProvider());
        assertEquals("POL123", result.getPolicyNumber());
        assertEquals("Self", result.getRelationship());
    }

    @Test
    void deleteInsuranceCard_delegatesToRepository() {
        insuranceCardService.deleteInsuranceCard("card4");

        verify(insuranceCardRepository).deleteById("card4");
    }
}
