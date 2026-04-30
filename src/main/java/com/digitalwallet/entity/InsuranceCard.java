package com.digitalwallet.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "insurance_cards")
public class InsuranceCard {
    @Id
    private String id;
    private String userId;
    private String providerName;
    private String policyNumber;
    private String groupNumber;
    private String memberName;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
    private String planType;
    private String frontCardImageBase64;
    private String backCardImageBase64;
    private int qrCodeData;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public InsuranceCard() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public String getGroupNumber() { return groupNumber; }
    public void setGroupNumber(String groupNumber) { this.groupNumber = groupNumber; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }

    public String getFrontCardImageBase64() { return frontCardImageBase64; }
    public void setFrontCardImageBase64(String frontCardImageBase64) { this.frontCardImageBase64 = frontCardImageBase64; }

    public String getBackCardImageBase64() { return backCardImageBase64; }
    public void setBackCardImageBase64(String backCardImageBase64) { this.backCardImageBase64 = backCardImageBase64; }

    public int getQrCodeData() { return qrCodeData; }
    public void setQrCodeData(int qrCodeData) { this.qrCodeData = qrCodeData; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
