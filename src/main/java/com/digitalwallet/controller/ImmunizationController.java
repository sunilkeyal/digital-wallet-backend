package com.digitalwallet.controller;

import com.digitalwallet.dto.ImmunizationDto;
import com.digitalwallet.service.ImmunizationService;
import com.digitalwallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/immunizations")
public class ImmunizationController {
    private final ImmunizationService immunizationService;
    private final UserService userService;

    public ImmunizationController(ImmunizationService immunizationService, UserService userService) {
        this.immunizationService = immunizationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllImmunizations(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "vaccineName") String sortBy,
                                                   @RequestParam(defaultValue = "asc") String sortDir) {
        String userId = userService.getCurrentUserId();
        if (size <= 0 || size > 1000) {
            // Return all records (ALL option)
            return ResponseEntity.ok(immunizationService.getByUserId(userId, sortBy, sortDir));
        }
        Page<ImmunizationDto> result = immunizationService.getByUserIdPaginated(userId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImmunizationDto> getImmunization(@PathVariable String id) {
        return ResponseEntity.ok(immunizationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ImmunizationDto> createImmunization(@Valid @RequestBody ImmunizationDto dto) {
        String userId = userService.getCurrentUserId();
        dto.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(immunizationService.createImmunization(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImmunizationDto> updateImmunization(@PathVariable String id,
                                                              @Valid @RequestBody ImmunizationDto dto) {
        return ResponseEntity.ok(immunizationService.updateImmunization(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImmunization(@PathVariable String id) {
        immunizationService.deleteImmunization(id);
        return ResponseEntity.noContent().build();
    }
}
