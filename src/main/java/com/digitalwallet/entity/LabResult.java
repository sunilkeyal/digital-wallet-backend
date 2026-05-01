package com.digitalwallet.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "lab_results")
public class LabResult {
    @Id
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

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public LabResult() {}

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

