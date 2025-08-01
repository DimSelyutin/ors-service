package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.api.SubscriptionsApi;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionFilterDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import com.innowise.swimdom.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * Controller for authentication.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController implements SubscriptionsApi {

    private final SubscriptionService subscriptionService;

    /**
     * POST /subscriptions : Create subscription.
     */
    @Override
    @PostMapping("/")
    public ResponseEntity<SubscriptionDTO> createSubscription(
        @Valid @RequestBody SubscriptionCreateDTO subscriptionCreateDTO
    ) {
        return new ResponseEntity<>(subscriptionService.createSubscription(subscriptionCreateDTO), HttpStatus.CREATED);
    }

    /**
     * DELETE /id : Delete subscription.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable("id") UUID id) {
        subscriptionService.deleteSubscription(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * POST /subscriptions/filter : Get all subscriptions with filter.
     */
    @Override
    @PostMapping("/filter")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscription(
        @Valid @RequestBody SubscriptionFilterDTO subscriptionFilterDTO
    ) {
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions(subscriptionFilterDTO);
        return ResponseEntity.ok(subscriptions);
    }

    /**
     * GET /subscriptions/{id} : Get subscription by ID.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable UUID id) {
        return new ResponseEntity<>(subscriptionService.getSubscriptionById(id), HttpStatus.OK);
    }

    /**
     * GET /subscriptions : Get all subscriptions.
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptions(SubscriptionFilterDTO subfilter) {
        List<SubscriptionDTO> subscriptions =
            subscriptionService.getAllSubscriptions(subfilter);
        return ResponseEntity.ok(subscriptions);
    }

    /**
     * PUT /subscriptions/{id} : Update subscription by ID.
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> updateSubscription(
        @PathVariable UUID id,
        @Valid @RequestBody SubscriptionUpdateDTO subscriptionUpdateDTO
    ) {
        return new ResponseEntity<>(subscriptionService.updateSubscription(subscriptionUpdateDTO), HttpStatus.OK);
    }
}
