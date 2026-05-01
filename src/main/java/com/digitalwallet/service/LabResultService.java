package com.digitalwallet.service;

import com.digitalwallet.dto.LabResultDto;
import com.digitalwallet.entity.LabResult;
import com.digitalwallet.repository.LabResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LabResultService {
    private final LabResultRepository labResultRepository;

    public LabResultService(LabResultRepository labResultRepository) {
        this.labResultRepository = labResultRepository;
    }

    public List<LabResultDto> getByUserId(String userId, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        return labResultRepository.findByUserId(userId, sort).stream()
                .map(this::mapToDto)
                .toList();
    }

    public Page<LabResultDto> getByUserIdPaginated(String userId, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LabResult> labResultPage = labResultRepository.findByUserId(userId, pageable);
        return labResultPage.map(this::mapToDto);
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

