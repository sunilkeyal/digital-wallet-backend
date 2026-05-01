package com.digitalwallet.service;

import com.digitalwallet.dto.LabResultDto;
import com.digitalwallet.entity.LabResult;
import com.digitalwallet.repository.LabResultRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LabResultService {
    private final LabResultRepository labResultRepository;

    public LabResultService(LabResultRepository labResultRepository) {
        this.labResultRepository = labResultRepository;
    }

    public List<LabResultDto> getByUserId(String userId) {
        return labResultRepository.findByUserIdOrderByTestDateDesc(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public LabResultDto getById(String id) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab result not found"));
        return mapToDto(labResult);
    }

    public LabResultDto createLabResult(LabResultDto dto) {
        LabResult labResult = mapToEntity(dto);
        labResult.setCreatedAt(LocalDateTime.now());
        labResult.setUpdatedAt(LocalDateTime.now());
        return mapToDto(labResultRepository.save(labResult));
    }

    public LabResultDto updateLabResult(String id, LabResultDto dto) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab result not found"));
        labResult.setTestName(dto.getTestName());
        labResult.setTestDate(dto.getTestDate());
        labResult.setLaboratory(dto.getLaboratory());
        labResult.setOrderingProvider(dto.getOrderingProvider());
        labResult.setResult(dto.getResult());
        labResult.setUnit(dto.getUnit());
        labResult.setReferenceRange(dto.getReferenceRange());
        labResult.setNotes(dto.getNotes());
        labResult.setUpdatedAt(LocalDateTime.now());
        return mapToDto(labResultRepository.save(labResult));
    }

    public void deleteLabResult(String id) {
        labResultRepository.deleteById(id);
    }

    private LabResultDto mapToDto(LabResult labResult) {
        LabResultDto dto = new LabResultDto();
        dto.setId(labResult.getId());
        dto.setUserId(labResult.getUserId());
        dto.setTestName(labResult.getTestName());
        dto.setTestDate(labResult.getTestDate());
        dto.setLaboratory(labResult.getLaboratory());
        dto.setOrderingProvider(labResult.getOrderingProvider());
        dto.setResult(labResult.getResult());
        dto.setUnit(labResult.getUnit());
        dto.setReferenceRange(labResult.getReferenceRange());
        dto.setNotes(labResult.getNotes());
        dto.setCreatedAt(labResult.getCreatedAt() != null ? labResult.getCreatedAt().toString() : null);
        dto.setUpdatedAt(labResult.getUpdatedAt() != null ? labResult.getUpdatedAt().toString() : null);
        return dto;
    }

    private LabResult mapToEntity(LabResultDto dto) {
        LabResult labResult = new LabResult();
        labResult.setId(dto.getId());
        labResult.setUserId(dto.getUserId());
        labResult.setTestName(dto.getTestName());
        labResult.setTestDate(dto.getTestDate());
        labResult.setLaboratory(dto.getLaboratory());
        labResult.setOrderingProvider(dto.getOrderingProvider());
        labResult.setResult(dto.getResult());
        labResult.setUnit(dto.getUnit());
        labResult.setReferenceRange(dto.getReferenceRange());
        labResult.setNotes(dto.getNotes());
        return labResult;
    }
}

