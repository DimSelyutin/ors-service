package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

/**
 * Repository for UserSubscription relations.
 */
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, UUID>,
        JpaSpecificationExecutor<UserSubscription> {

    List<UserSubscription> findByUserId(UUID userId);
}


