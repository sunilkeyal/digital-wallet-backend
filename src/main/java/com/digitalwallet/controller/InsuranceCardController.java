package com.digitalwallet.controller;

import com.digitalwallet.dto.InsuranceCardDto;
import com.digitalwallet.service.InsuranceCardService;
import com.digitalwallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance-cards")
public class InsuranceCardController {
    private final InsuranceCardService insuranceCardService;
    private final UserService userService;

    public InsuranceCardController(InsuranceCardService insuranceCardService, UserService userService) {
        this.insuranceCardService = insuranceCardService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<InsuranceCardDto>> getAllInsuranceCards() {
        String userId = userService.getCurrentUserId();
        return ResponseEntity.ok(insuranceCardService.getByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCardDto> getInsuranceCard(@PathVariable String id) {
        return ResponseEntity.ok(insuranceCardService.getById(id));
    }

    @PostMapping
    public ResponseEntity<InsuranceCardDto> createInsuranceCard(@Valid @RequestBody InsuranceCardDto dto) {
        String userId = userService.getCurrentUserId();
        dto.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(insuranceCardService.createInsuranceCard(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCardDto> updateInsuranceCard(@PathVariable String id,
                                                                @Valid @RequestBody InsuranceCardDto dto) {
        return ResponseEntity.ok(insuranceCardService.updateInsuranceCard(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCard(@PathVariable String id) {
        insuranceCardService.deleteInsuranceCard(id);
        return ResponseEntity.noContent().build();
    }
}
