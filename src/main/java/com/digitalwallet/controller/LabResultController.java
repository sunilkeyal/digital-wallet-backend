package com.digitalwallet.controller;

import com.digitalwallet.dto.LabResultDto;
import com.digitalwallet.service.LabResultService;
import com.digitalwallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<?> getAllLabResults(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "testDate") String sortBy,
                                             @RequestParam(defaultValue = "desc") String sortDir) {
        String userId = userService.getCurrentUserId();
        if (size <= 0 || size > 1000) {
            return ResponseEntity.ok(labResultService.getByUserId(userId, sortBy, sortDir));
        }
        Page<LabResultDto> result = labResultService.getByUserIdPaginated(userId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(result);
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
