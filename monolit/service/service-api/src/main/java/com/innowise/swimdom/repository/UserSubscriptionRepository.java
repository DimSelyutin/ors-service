package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for Subscriptions.
 */
@Repository
public interface UserSubscriptionRepository
    extends JpaRepository<UserSubscription, UUID> {

}
