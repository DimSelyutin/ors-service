package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionFilterDTO;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;

import java.util.List;
import java.util.UUID;

/**
 * Subscription management service.
 * Provides methods for creating, receiving, updating, and deleting subscriptions.
 */
public interface SubscriptionService {

    /**
     * Get a list of all subscriptions for admin.
     *
     * @return list of DTO subscriptions; empty list if there are no subscriptions
     */
    List<SubscriptionDTO> getAllSubscriptions(SubscriptionFilterDTO subscriptionDTO);

    /**
     * Get a subscription using a unique identifier.
     *
     * @param id UUID
     * @return DTO subscriptions
     */
    SubscriptionDTO getSubscriptionById(UUID id);

    /**
     * Create a new subscription.
     *
     * @param createDTO DTO with subscription creation data
     * @return DTO of the created subscription
     */
    SubscriptionDTO createSubscription(SubscriptionCreateDTO createDTO);

    /**
     * Update your existing subscription.
     *
     * @param updateDTO DTO with updated subscription data
     * @return DTO of the renewed subscription
     */
    SubscriptionDTO updateSubscription(SubscriptionUpdateDTO updateDTO);

    /**
     * Delete a subscription by ID.
     *
     * @param id UUID
     */
    void deleteSubscription(UUID id);
}
