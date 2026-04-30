package com.digitalwallet.config;

import com.digitalwallet.entity.Immunization;
import com.digitalwallet.repository.ImmunizationRepository;
import com.digitalwallet.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ImmunizationRepository immunizationRepository;

    public DataInitializer(UserRepository userRepository, ImmunizationRepository immunizationRepository) {
        this.userRepository = userRepository;
        this.immunizationRepository = immunizationRepository;
    }

    @Override
    public void run(String... args) {
        var admin = userRepository.findByEmail("admin@digitalwallet.com");
        if (admin.isPresent()) {
            String userId = admin.get().getId();
            if (immunizationRepository.findByUserId(userId).isEmpty()) {
                List<Immunization> sampleData = createSampleImmunizations(userId);
                immunizationRepository.saveAll(sampleData);
                System.out.println("Added " + sampleData.size() + " sample immunization records");
            }
        }
    }

    private List<Immunization> createSampleImmunizations(String userId) {
        List<Immunization> records = new ArrayList<>();
        
        String[] vaccines = {
            "COVID-19 Vaccine (Pfizer)", "COVID-19 Vaccine (Moderna)", "Influenza (Flu)", 
            "MMR (Measles, Mumps, Rubella)", "Polio Vaccine", "Hepatitis B", 
            "Tetanus, Diphtheria, Pertussis (Tdap)", "Varicella (Chickenpox)", 
            "HPV (Gardasil 9)", "Meningococcal (MenACWY)", "Pneumococcal (PCV13)",
            "Rotavirus", "Hepatitis A", "Haemophilus influenzae type b (Hib)",
            "Japanese Encephalitis", "Rabies Vaccine", "Yellow Fever",
            "Typhoid Vaccine", "Cholera Vaccine", "BCG (Tuberculosis)",
            "Smallpox Vaccine", "Anthrax Vaccine", "Shingles (Zoster)", 
            "Dengue Vaccine", "RSV Vaccine", "Malaria Vaccine",
            "Ebola Vaccine", "Chikungunya Vaccine", "Zika Vaccine",
            "COVID-19 Booster (Pfizer)", "COVID-19 Booster (Moderna)", "Hepatitis A+B Combo"
        };
        
        String[] manufacturers = {
            "Pfizer-BioNTech", "Moderna", "Sanofi Pasteur", "Merck", "Sanofi Pasteur",
            "GSK", "Sanofi Pasteur", "Merck", "Merck", "Sanofi Pasteur",
            "Pfizer", "Merck", "GSK", "Sanofi Pasteur", "Sanofi Pasteur",
            "Sanofi Pasteur", "Sanofi Pasteur", "Sanofi Pasteur", "Sanofi Pasteur",
            "Merck", "Sanofi Pasteur", "GSK", "Bharat Biotech", "Merck",
            "Sanofi Pasteur", "Pfizer-BioNTech", "Moderna", "GSK"
        };
        
        String[] facilities = {
            "City General Hospital", "Community Health Clinic", "Downtown Medical Center",
            "University Health Services", "Pediatric Wellness Center", "Travel Health Clinic",
            "Occupational Health Dept", "School Immunization Clinic", "Public Health Unit",
            "Family Medicine Practice", "City General Hospital", "Community Health Clinic",
            "Downtown Medical Center", "University Health Services", "Pediatric Wellness Center",
            "Travel Health Clinic", "Occupational Health Dept", "School Immunization Clinic",
            "Public Health Unit", "Family Medicine Practice", "City General Hospital",
            "Community Health Clinic", "Downtown Medical Center", "University Health Services",
            "Pediatric Wellness Center", "Travel Health Clinic", "Occupational Health Dept"
        };

        LocalDate baseDate = LocalDate.of(2018, 1, 15);
        
        for (int i = 0; i < 30; i++) {
            Immunization imm = new Immunization();
            imm.setUserId(userId);
            imm.setVaccineName(vaccines[i]);
            imm.setManufacturer(manufacturers[i % manufacturers.length]);
            imm.setLotNumber("LOT-" + (100000 + i));
            imm.setAdministrationDate(baseDate.plusMonths(i));
            String doc = (i % 2 == 0) ? "Dr. Smith" : "Dr. Johnson";
            imm.setAdministeredBy(doc);
            imm.setFacilityName(facilities[i % facilities.length]);
            imm.setFacilityAddress("123 Main St, Suite " + (i + 1) + ", City, State");
            imm.setNotes("Sample immunization record #" + (i + 1));
            records.add(imm);
        }
        
        return records;
    }
}
