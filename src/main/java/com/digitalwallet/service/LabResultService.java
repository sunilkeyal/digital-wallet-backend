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
        labResult.setLaboratoryName(dto.getLaboratoryName());
        labResult.setOrderingPhysician(dto.getOrderingPhysician());
        labResult.setStatus(dto.getStatus());
        labResult.setDigitalSignature(dto.getDigitalSignature());
        labResult.setNotes(dto.getNotes());
        labResult.setVitalStatistics(dto.getVitalStatistics() != null ?
                dto.getVitalStatistics().stream()
                        .map(v -> {
                            LabResult.VitalStatistic vs = new LabResult.VitalStatistic();
                            vs.setName(v.getName());
                            vs.setValue(v.getValue());
                            vs.setUnit(v.getUnit());
                            vs.setStatus(v.getStatus());
                            return vs;
                        }).toList() : null);
        labResult.setTestResultValues(dto.getTestResultValues() != null ?
                dto.getTestResultValues().stream()
                        .map(t -> {
                            LabResult.TestResultValue tv = new LabResult.TestResultValue();
                            tv.setTestName(t.getTestName());
                            tv.setResult(t.getResult());
                            tv.setUnit(t.getUnit());
                            tv.setReferenceRange(t.getReferenceRange());
                            tv.setFlag(t.getFlag());
                            return tv;
                        }).toList() : null);
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
        dto.setLaboratoryName(labResult.getLaboratoryName());
        dto.setOrderingPhysician(labResult.getOrderingPhysician());
        dto.setStatus(labResult.getStatus());
        dto.setDigitalSignature(labResult.getDigitalSignature());
        dto.setNotes(labResult.getNotes());
        dto.setCreatedAt(labResult.getCreatedAt() != null ? labResult.getCreatedAt().toString() : null);
        dto.setUpdatedAt(labResult.getUpdatedAt() != null ? labResult.getUpdatedAt().toString() : null);

        if (labResult.getVitalStatistics() != null) {
            dto.setVitalStatistics(labResult.getVitalStatistics().stream().map(vs -> {
                LabResultDto.VitalStatisticDto vDto = new LabResultDto.VitalStatisticDto();
                vDto.setName(vs.getName());
                vDto.setValue(vs.getValue());
                vDto.setUnit(vs.getUnit());
                vDto.setStatus(vs.getStatus());
                return vDto;
            }).toList());
        }

        if (labResult.getTestResultValues() != null) {
            dto.setTestResultValues(labResult.getTestResultValues().stream().map(tv -> {
                LabResultDto.TestResultValueDto tDto = new LabResultDto.TestResultValueDto();
                tDto.setTestName(tv.getTestName());
                tDto.setResult(tv.getResult());
                tDto.setUnit(tv.getUnit());
                tDto.setReferenceRange(tv.getReferenceRange());
                tDto.setFlag(tv.getFlag());
                return tDto;
            }).toList());
        }

        return dto;
    }

    private LabResult mapToEntity(LabResultDto dto) {
        LabResult labResult = new LabResult();
        labResult.setId(dto.getId());
        labResult.setUserId(dto.getUserId());
        labResult.setTestName(dto.getTestName());
        labResult.setTestDate(dto.getTestDate());
        labResult.setLaboratoryName(dto.getLaboratoryName());
        labResult.setOrderingPhysician(dto.getOrderingPhysician());
        labResult.setStatus(dto.getStatus());
        labResult.setDigitalSignature(dto.getDigitalSignature());
        labResult.setNotes(dto.getNotes());

        if (dto.getVitalStatistics() != null) {
            labResult.setVitalStatistics(dto.getVitalStatistics().stream().map(v -> {
                LabResult.VitalStatistic vs = new LabResult.VitalStatistic();
                vs.setName(v.getName());
                vs.setValue(v.getValue());
                vs.setUnit(v.getUnit());
                vs.setStatus(v.getStatus());
                return vs;
            }).toList());
        }

        if (dto.getTestResultValues() != null) {
            labResult.setTestResultValues(dto.getTestResultValues().stream().map(t -> {
                LabResult.TestResultValue tv = new LabResult.TestResultValue();
                tv.setTestName(t.getTestName());
                tv.setResult(t.getResult());
                tv.setUnit(t.getUnit());
                tv.setReferenceRange(t.getReferenceRange());
                tv.setFlag(t.getFlag());
                return tv;
            }).toList());
        }

        return labResult;
    }
}
