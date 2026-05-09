package com.digitalwallet.service;

import com.digitalwallet.dto.LabResultDto;
import com.digitalwallet.entity.LabResult;
import com.digitalwallet.repository.LabResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LabResultServiceTest {

    @Mock
    private LabResultRepository labResultRepository;

    @InjectMocks
    private LabResultService labResultService;

    @Test
    void getByUserId_returnsDtoList() {
        LabResult entity = new LabResult();
        entity.setId("lab1");
        entity.setUserId("user1");
        entity.setTestName("CBC");
        entity.setTestDate(LocalDate.of(2024, 5, 10));
        entity.setLaboratory("City Lab");

        when(labResultRepository.findByUserId("user1", Sort.by(Sort.Direction.ASC, "testName")))
                .thenReturn(List.of(entity));

        List<LabResultDto> result = labResultService.getByUserId("user1", "testName", "asc");

        assertEquals(1, result.size());
        assertEquals("lab1", result.get(0).getId());
        assertEquals("CBC", result.get(0).getTestName());
    }

    @Test
    void getByUserIdPaginated_returnsPageOfDtos() {
        LabResult entity = new LabResult();
        entity.setId("lab2");
        entity.setUserId("user1");
        entity.setTestName("Cholesterol");

        Page<LabResult> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "testDate")), 1);
        when(labResultRepository.findByUserId("user1", PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "testDate"))))
                .thenReturn(page);

        Page<LabResultDto> result = labResultService.getByUserIdPaginated("user1", 0, 10, "testDate", "desc");

        assertEquals(1, result.getTotalElements());
        assertEquals("lab2", result.getContent().get(0).getId());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(labResultRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> labResultService.getById("missing"));
    }

    @Test
    void createLabResult_setsTimestampsAndSaves() {
        LabResultDto dto = new LabResultDto();
        dto.setUserId("user2");
        dto.setTestName("Glucose");

        when(labResultRepository.save(any(LabResult.class))).thenAnswer(invocation -> {
            LabResult saved = invocation.getArgument(0);
            saved.setId("saved-id");
            return saved;
        });

        LabResultDto result = labResultService.createLabResult(dto);

        assertEquals("saved-id", result.getId());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void updateLabResult_updatesFieldsAndReturnsDto() {
        LabResult existing = new LabResult();
        existing.setId("lab3");
        existing.setTestName("Old Test");
        existing.setUserId("user1");
        existing.setCreatedAt(LocalDateTime.of(2024, 1, 1, 0, 0));

        LabResultDto dto = new LabResultDto();
        dto.setTestName("Updated Test");
        dto.setTestDate(LocalDate.of(2024, 6, 1));
        dto.setLaboratory("Updated Lab");
        dto.setOrderingProvider("Provider");
        dto.setResult("Positive");
        dto.setUnit("mg/dL");
        dto.setReferenceRange("4-8");
        dto.setNotes("Updated note");

        when(labResultRepository.findById("lab3")).thenReturn(Optional.of(existing));
        when(labResultRepository.save(existing)).thenReturn(existing);

        LabResultDto result = labResultService.updateLabResult("lab3", dto);

        assertEquals("Updated Test", result.getTestName());
        assertEquals("Updated Lab", result.getLaboratory());
        assertEquals("Positive", result.getResult());
    }

    @Test
    void deleteLabResult_delegatesToRepository() {
        labResultService.deleteLabResult("lab4");

        verify(labResultRepository).deleteById("lab4");
    }
}
