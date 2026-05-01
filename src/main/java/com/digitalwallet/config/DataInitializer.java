package com.digitalwallet.config;

import com.digitalwallet.entity.Immunization;
import com.digitalwallet.entity.InsuranceCard;
import com.digitalwallet.entity.LabResult;
import com.digitalwallet.repository.ImmunizationRepository;
import com.digitalwallet.repository.InsuranceCardRepository;
import com.digitalwallet.repository.LabResultRepository;
import com.digitalwallet.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final ImmunizationRepository immunizationRepository;
    private final InsuranceCardRepository insuranceCardRepository;
    private final LabResultRepository labResultRepository;

    public DataInitializer(UserService userService,
                           ImmunizationRepository immunizationRepository,
                           InsuranceCardRepository insuranceCardRepository,
                           LabResultRepository labResultRepository) {
        this.userService = userService;
        this.immunizationRepository = immunizationRepository;
        this.insuranceCardRepository = insuranceCardRepository;
        this.labResultRepository = labResultRepository;
    }

    @Override
    public void run(String... args) {
        var admin = userService.createAdminUser();
        System.out.println("Admin user ready: " + admin.getEmail());
    }

    public void seedForUser(String userId) {
        immunizationRepository.saveAll(createSampleImmunizations(userId));
        insuranceCardRepository.saveAll(createSampleInsuranceCards(userId));
        labResultRepository.saveAll(createSampleLabResults(userId));
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

    private List<InsuranceCard> createSampleInsuranceCards(String userId) {
        List<InsuranceCard> cards = new ArrayList<>();

        String[][] plans = {
            {"UnitedHealthcare", "PPO", "UHC-PPO-001"},
            {"Blue Cross Blue Shield", "HMO", "BCBS-HMO-002"},
            {"Aetna", "EPO", "AET-EPO-003"},
            {"Cigna", "HDHP", "CIG-HDHP-004"},
            {"Humana", "POS", "HUM-POS-005"}
        };

        for (int i = 0; i < plans.length; i++) {
            InsuranceCard card = new InsuranceCard();
            card.setUserId(userId);
            card.setProvider(plans[i][0]);
            card.setPolicyNumber(plans[i][2]);
            card.setGroupNumber("GRP-" + (10000 + i));
            card.setMemberName("Admin User");
            card.setEffectiveDate(LocalDate.of(2024, 1, 1).plusYears(i % 2));
            card.setExpiryDate(LocalDate.of(2025, 12, 31).plusYears(i % 2));
            cards.add(card);
        }

        return cards;
    }

    private List<LabResult> createSampleLabResults(String userId) {
        List<LabResult> results = new ArrayList<>();

        String[][] tests = {
            {"Complete Blood Count (CBC)", "City General Hospital", "Dr. Smith"},
            {"Comprehensive Metabolic Panel", "Downtown Medical Center", "Dr. Johnson"},
            {"Lipid Panel", "Community Health Clinic", "Dr. Patel"},
            {"Thyroid Function (TSH)", "University Health Services", "Dr. Lee"},
            {"Hemoglobin A1c", "Family Medicine Practice", "Dr. Smith"},
            {"Urinalysis", "City General Hospital", "Dr. Johnson"},
            {"Vitamin D, 25-Hydroxy", "Community Health Clinic", "Dr. Patel"},
            {"Basic Metabolic Panel", "Downtown Medical Center", "Dr. Lee"}
        };

        LocalDate baseDate = LocalDate.of(2024, 1, 10);

        for (int i = 0; i < tests.length; i++) {
            LabResult result = new LabResult();
            result.setUserId(userId);
            result.setTestName(tests[i][0]);
            result.setLaboratory(tests[i][1]);
            result.setOrderingProvider(tests[i][2]);
            result.setTestDate(baseDate.plusMonths(i));
            result.setResult(String.valueOf(5.0 + i * 0.3));
            result.setUnit("mg/dL");
            result.setReferenceRange("3.5 - 10.0");
            result.setNotes("Sample lab result #" + (i + 1));
            results.add(result);
        }

        return results;
    }
}
