package com.digitalwallet.dto;

import java.time.LocalDate;

public class LabResultDto {
    private String id;
    private String userId;
    private String testName;
    private LocalDate testDate;
    private String laboratory;
    private String orderingProvider;
    private String result;
    private String unit;
    private String referenceRange;
    private String notes;
    private String createdAt;
    private String updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public LocalDate getTestDate() { return testDate; }
    public void setTestDate(LocalDate testDate) { this.testDate = testDate; }
    public String getLaboratory() { return laboratory; }
    public void setLaboratory(String laboratory) { this.laboratory = laboratory; }
    public String getOrderingProvider() { return orderingProvider; }
    public void setOrderingProvider(String orderingProvider) { this.orderingProvider = orderingProvider; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getReferenceRange() { return referenceRange; }
    public void setReferenceRange(String referenceRange) { this.referenceRange = referenceRange; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}

