package com.digitalwallet.dto;

import java.time.LocalDate;
import java.util.List;

public class LabResultDto {
    private String id;
    private String userId;
    private String testName;
    private LocalDate testDate;
    private String laboratoryName;
    private String orderingPhysician;
    private String status;
    private String digitalSignature;
    private String notes;
    private List<VitalStatisticDto> vitalStatistics;
    private List<TestResultValueDto> testResultValues;
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
    public String getLaboratoryName() { return laboratoryName; }
    public void setLaboratoryName(String laboratoryName) { this.laboratoryName = laboratoryName; }
    public String getOrderingPhysician() { return orderingPhysician; }
    public void setOrderingPhysician(String orderingPhysician) { this.orderingPhysician = orderingPhysician; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDigitalSignature() { return digitalSignature; }
    public void setDigitalSignature(String digitalSignature) { this.digitalSignature = digitalSignature; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<VitalStatisticDto> getVitalStatistics() { return vitalStatistics; }
    public void setVitalStatistics(List<VitalStatisticDto> vitalStatistics) { this.vitalStatistics = vitalStatistics; }
    public List<TestResultValueDto> getTestResultValues() { return testResultValues; }
    public void setTestResultValues(List<TestResultValueDto> testResultValues) { this.testResultValues = testResultValues; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public static class VitalStatisticDto {
        private String name;
        private String value;
        private String unit;
        private String status;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class TestResultValueDto {
        private String testName;
        private String result;
        private String unit;
        private String referenceRange;
        private String flag;

        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public String getReferenceRange() { return referenceRange; }
        public void setReferenceRange(String referenceRange) { this.referenceRange = referenceRange; }
        public String getFlag() { return flag; }
        public void setFlag(String flag) { this.flag = flag; }
    }
}
