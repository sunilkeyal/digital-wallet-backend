package com.digitalwallet.service;

import com.digitalwallet.dto.ImmunizationDto;
import com.digitalwallet.entity.Immunization;
import com.digitalwallet.repository.ImmunizationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImmunizationService {
    private final ImmunizationRepository immunizationRepository;

    public ImmunizationService(ImmunizationRepository immunizationRepository) {
        this.immunizationRepository = immunizationRepository;
    }

    public List<ImmunizationDto> getByUserId(String userId) {
        return immunizationRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public Page<ImmunizationDto> getByUserIdPaginated(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Immunization> immunizationPage = immunizationRepository.findByUserId(userId, pageable);
        return immunizationPage.map(this::mapToDto);
    }

    public ImmunizationDto getById(String id) {
        Immunization immunization = immunizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Immunization not found"));
        return mapToDto(immunization);
    }

    public ImmunizationDto createImmunization(ImmunizationDto dto) {
        Immunization immunization = mapToEntity(dto);
        immunization.setCreatedAt(LocalDateTime.now());
        immunization.setUpdatedAt(LocalDateTime.now());
        return mapToDto(immunizationRepository.save(immunization));
    }

    public ImmunizationDto updateImmunization(String id, ImmunizationDto dto) {
        Immunization immunization = immunizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Immunization not found"));
        immunization.setVaccineName(dto.getVaccineName());
        immunization.setManufacturer(dto.getManufacturer());
        immunization.setLotNumber(dto.getLotNumber());
        immunization.setAdministrationDate(dto.getAdministrationDate());
        immunization.setAdministeredBy(dto.getAdministeredBy());
        immunization.setFacilityName(dto.getFacilityName());
        immunization.setFacilityAddress(dto.getFacilityAddress());
        immunization.setNotes(dto.getNotes());
        immunization.setUpdatedAt(LocalDateTime.now());
        return mapToDto(immunizationRepository.save(immunization));
    }

    public void deleteImmunization(String id) {
        immunizationRepository.deleteById(id);
    }

    private ImmunizationDto mapToDto(Immunization immunization) {
        ImmunizationDto dto = new ImmunizationDto();
        dto.setId(immunization.getId());
        dto.setUserId(immunization.getUserId());
        dto.setVaccineName(immunization.getVaccineName());
        dto.setManufacturer(immunization.getManufacturer());
        dto.setLotNumber(immunization.getLotNumber());
        dto.setAdministrationDate(immunization.getAdministrationDate());
        dto.setAdministeredBy(immunization.getAdministeredBy());
        dto.setFacilityName(immunization.getFacilityName());
        dto.setFacilityAddress(immunization.getFacilityAddress());
        dto.setNotes(immunization.getNotes());
        dto.setCreatedAt(immunization.getCreatedAt() != null ? immunization.getCreatedAt().toString() : null);
        dto.setUpdatedAt(immunization.getUpdatedAt() != null ? immunization.getUpdatedAt().toString() : null);
        return dto;
    }

    private Immunization mapToEntity(ImmunizationDto dto) {
        Immunization immunization = new Immunization();
        immunization.setId(dto.getId());
        immunization.setUserId(dto.getUserId());
        immunization.setVaccineName(dto.getVaccineName());
        immunization.setManufacturer(dto.getManufacturer());
        immunization.setLotNumber(dto.getLotNumber());
        immunization.setAdministrationDate(dto.getAdministrationDate());
        immunization.setAdministeredBy(dto.getAdministeredBy());
        immunization.setFacilityName(dto.getFacilityName());
        immunization.setFacilityAddress(dto.getFacilityAddress());
        immunization.setNotes(dto.getNotes());
        return immunization;
    }
}
