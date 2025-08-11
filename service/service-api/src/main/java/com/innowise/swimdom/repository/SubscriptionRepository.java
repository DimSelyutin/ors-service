package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.Subscription;
<<<<<<< Updated upstream
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

>>>>>>> Stashed changes
import java.util.UUID;

/**
 * Repository for Subscriptions.
 */
@Repository
public interface SubscriptionRepository
<<<<<<< Updated upstream
    extends JpaRepository<Subscription, UUID>, JpaSpecificationExecutor<Subscription> {

    /**
     * Method for searching subscriptions.
     *
     * @param spec filter
     * @return list with subscriptions that match to filters
     */
    List<Subscription> findAll(Specification<Subscription> spec);
=======
    extends JpaRepository<Subscription, UUID> {
>>>>>>> Stashed changes

}
