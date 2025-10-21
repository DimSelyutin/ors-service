package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;
import com.innowise.swimdom.service.UserSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * COntroller forsubscroptions.
 */
@RestController
@RequestMapping("/api/v1/user/subscriptions")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    @GetMapping
    public ResponseEntity<List<UserSubscriptionDTO>> getAll() {
        return ResponseEntity.ok(userSubscriptionService.getAll());
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<UserSubscriptionDTO>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userSubscriptionService.getByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSubscriptionDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userSubscriptionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UserSubscriptionDTO> create(@Valid @RequestBody UserSubscriptionCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userSubscriptionService.create(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSubscriptionDTO> update(
        @PathVariable UUID id,
        @Valid @RequestBody UserSubscriptionUpdateDTO updateDTO
    ) {
        return ResponseEntity.ok(userSubscriptionService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userSubscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


