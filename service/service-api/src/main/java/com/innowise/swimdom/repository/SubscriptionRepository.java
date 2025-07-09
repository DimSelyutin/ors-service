package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for Subscriptions.
 */
@Repository
public interface SubscriptionRepository
    extends JpaRepository<Subscription, UUID> {

}
