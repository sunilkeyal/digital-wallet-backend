package com.digitalwallet.service;

import com.digitalwallet.dto.InsuranceCardDto;
import com.digitalwallet.entity.InsuranceCard;
import com.digitalwallet.repository.InsuranceCardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InsuranceCardService {
    private final InsuranceCardRepository insuranceCardRepository;

    public InsuranceCardService(InsuranceCardRepository insuranceCardRepository) {
        this.insuranceCardRepository = insuranceCardRepository;
    }

    public List<InsuranceCardDto> getByUserId(String userId) {
        return insuranceCardRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public InsuranceCardDto getById(String id) {
        InsuranceCard card = insuranceCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance card not found"));
        return mapToDto(card);
    }

    public InsuranceCardDto createInsuranceCard(InsuranceCardDto dto) {
        InsuranceCard card = mapToEntity(dto);
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        return mapToDto(insuranceCardRepository.save(card));
    }

    public InsuranceCardDto updateInsuranceCard(String id, InsuranceCardDto dto) {
        InsuranceCard card = insuranceCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance card not found"));
        card.setProviderName(dto.getProviderName());
        card.setPolicyNumber(dto.getPolicyNumber());
        card.setGroupNumber(dto.getGroupNumber());
        card.setMemberName(dto.getMemberName());
        card.setEffectiveDate(dto.getEffectiveDate());
        card.setExpirationDate(dto.getExpirationDate());
        card.setPlanType(dto.getPlanType());
        card.setFrontCardImageBase64(dto.getFrontCardImageBase64());
        card.setBackCardImageBase64(dto.getBackCardImageBase64());
        card.setQrCodeData(dto.getQrCodeData());
        card.setUpdatedAt(LocalDateTime.now());
        return mapToDto(insuranceCardRepository.save(card));
    }

    public void deleteInsuranceCard(String id) {
        insuranceCardRepository.deleteById(id);
    }

    private InsuranceCardDto mapToDto(InsuranceCard card) {
        InsuranceCardDto dto = new InsuranceCardDto();
        dto.setId(card.getId());
        dto.setUserId(card.getUserId());
        dto.setProviderName(card.getProviderName());
        dto.setPolicyNumber(card.getPolicyNumber());
        dto.setGroupNumber(card.getGroupNumber());
        dto.setMemberName(card.getMemberName());
        dto.setEffectiveDate(card.getEffectiveDate());
        dto.setExpirationDate(card.getExpirationDate());
        dto.setPlanType(card.getPlanType());
        dto.setFrontCardImageBase64(card.getFrontCardImageBase64());
        dto.setBackCardImageBase64(card.getBackCardImageBase64());
        dto.setQrCodeData(card.getQrCodeData());
        dto.setCreatedAt(card.getCreatedAt() != null ? card.getCreatedAt().toString() : null);
        dto.setUpdatedAt(card.getUpdatedAt() != null ? card.getUpdatedAt().toString() : null);
        return dto;
    }

    private InsuranceCard mapToEntity(InsuranceCardDto dto) {
        InsuranceCard card = new InsuranceCard();
        card.setId(dto.getId());
        card.setUserId(dto.getUserId());
        card.setProviderName(dto.getProviderName());
        card.setPolicyNumber(dto.getPolicyNumber());
        card.setGroupNumber(dto.getGroupNumber());
        card.setMemberName(dto.getMemberName());
        card.setEffectiveDate(dto.getEffectiveDate());
        card.setExpirationDate(dto.getExpirationDate());
        card.setPlanType(dto.getPlanType());
        card.setFrontCardImageBase64(dto.getFrontCardImageBase64());
        card.setBackCardImageBase64(dto.getBackCardImageBase64());
        card.setQrCodeData(dto.getQrCodeData());
        return card;
    }
}
