package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

/**
 * A service for managing user subscriptions (UserSubscription).
 * Provides methods for creating, receiving, updating, and deleting user subscriptions.
 */
public interface UserSubscriptionService {

    /**
     * Get a list of all user subscriptions.
     *
     * @return list of DTO user subscriptions; empty list if there are no subscriptions
     */
    List<UserSubscriptionDTO> getAllUserSubscriptions(Pageable pageable);

    /**
     * Get a user subscription using a unique identifier.
     *
     * @param id UUID of the user subscription
     * @return DTO of the user subscription
     */
    UserSubscriptionDTO getUserSubscriptionById(UUID id);

    /**
     * Create a new user subscription.
     *
     * @param createDTO DTO with data for creating a custom subscription
     * @return DTO of the created user subscription
     */
    UserSubscriptionDTO createUserSubscription(UserSubscriptionCreateDTO createDTO);

    /**
     * Upgrade your existing user subscription.
     *
     * @param id UUID of the user subscription for renewal
     * @param updateDTO DTO with updated user subscription data
     * @return DTO of the renewed user subscription
     */
    UserSubscriptionDTO updateUserSubscription(UUID id, UserSubscriptionUpdateDTO updateDTO);

    /**
     * Delete a user subscription by ID.
     *
     * @param id UUID of the user subscription
     */
    void deleteUserSubscription(UUID id);
}
