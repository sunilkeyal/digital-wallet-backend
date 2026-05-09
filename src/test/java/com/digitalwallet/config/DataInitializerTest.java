package com.digitalwallet.config;

import com.digitalwallet.entity.Immunization;
import com.digitalwallet.entity.InsuranceCard;
import com.digitalwallet.entity.LabResult;
import com.digitalwallet.entity.User;
import com.digitalwallet.repository.ImmunizationRepository;
import com.digitalwallet.repository.InsuranceCardRepository;
import com.digitalwallet.repository.LabResultRepository;
import com.digitalwallet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UserService userService;

    @Mock
    private ImmunizationRepository immunizationRepository;

    @Mock
    private InsuranceCardRepository insuranceCardRepository;

    @Mock
    private LabResultRepository labResultRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void run_callsCreateAdminUser() {
        when(userService.createAdminUser()).thenReturn(new User());

        dataInitializer.run();

        verify(userService).createAdminUser();
    }

    @Test
    void seedForUser_savesAllSampleRecords() {
        dataInitializer.seedForUser("user1");

        ArgumentCaptor<List<Immunization>> immunizationCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<InsuranceCard>> insuranceCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<LabResult>> labResultCaptor = ArgumentCaptor.forClass(List.class);

        verify(immunizationRepository).saveAll(immunizationCaptor.capture());
        verify(insuranceCardRepository).saveAll(insuranceCaptor.capture());
        verify(labResultRepository).saveAll(labResultCaptor.capture());

        assertEquals(30, immunizationCaptor.getValue().size());
        assertEquals(5, insuranceCaptor.getValue().size());
        assertEquals(8, labResultCaptor.getValue().size());
    }
}
