package com.digitalwallet.dto;

import java.time.LocalDate;

public class ImmunizationDto {
    private String id;
    private String userId;
    private String vaccineName;
    private String manufacturer;
    private String lotNumber;
    private String administrationDate;
    private String administeredBy;
    private String facilityName;
    private String facilityAddress;
    private String notes;
    private String createdAt;
    private String updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getVaccineName() { return vaccineName; }
    public void setVaccineName(String vaccineName) { this.vaccineName = vaccineName; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getAdministrationDate() { return administrationDate; }
    public void setAdministrationDate(String administrationDate) { this.administrationDate = administrationDate; }
    public String getAdministeredBy() { return administeredBy; }
    public void setAdministeredBy(String administeredBy) { this.administeredBy = administeredBy; }
    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    public String getFacilityAddress() { return facilityAddress; }
    public void setFacilityAddress(String facilityAddress) { this.facilityAddress = facilityAddress; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
