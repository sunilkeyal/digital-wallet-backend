package com.digitalwallet.repository;

import com.digitalwallet.entity.Immunization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ImmunizationRepositoryTest {
    @Autowired
    private ImmunizationRepository repository;

    @Test
    void findByUserId_returnsImmunizations() {
        Immunization immunization = new Immunization();
        immunization.setUserId("user123");
        immunization.setVaccineName("Flu Shot");
        immunization.setAdministrationDate(LocalDate.of(2024, 1, 15));
        repository.save(immunization);

        List<Immunization> results = repository.findByUserId("user123");

        assertFalse(results.isEmpty());
        assertEquals("Flu Shot", results.get(0).getVaccineName());
    }

    @Test
    void findByUserId_returnsEmptyListWhenNoRecords() {
        List<Immunization> results = repository.findByUserId("nonexistent");

        assertTrue(results.isEmpty());
    }
}
