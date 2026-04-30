package com.digitalwallet.controller;

import com.digitalwallet.dto.LabResultDto;
import com.digitalwallet.service.LabResultService;
import com.digitalwallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-results")
public class LabResultController {
    private final LabResultService labResultService;
    private final UserService userService;

    public LabResultController(LabResultService labResultService, UserService userService) {
        this.labResultService = labResultService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<LabResultDto>> getAllLabResults() {
        String userId = userService.getCurrentUserId();
        return ResponseEntity.ok(labResultService.getByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabResultDto> getLabResult(@PathVariable String id) {
        return ResponseEntity.ok(labResultService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LabResultDto> createLabResult(@Valid @RequestBody LabResultDto dto) {
        String userId = userService.getCurrentUserId();
        dto.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(labResultService.createLabResult(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabResultDto> updateLabResult(@PathVariable String id,
                                                        @Valid @RequestBody LabResultDto dto) {
        return ResponseEntity.ok(labResultService.updateLabResult(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabResult(@PathVariable String id) {
        labResultService.deleteLabResult(id);
        return ResponseEntity.noContent().build();
    }
}
