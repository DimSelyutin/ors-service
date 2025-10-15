package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.PoolWorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

/**
 * Repository for get entity.
 */
public interface PoolWorkingHoursRepository extends JpaRepository<PoolWorkingHours, UUID> {

    Set<PoolWorkingHours> findPoolById(UUID id);

    void deleteAllByPoolId(UUID poolId);

}


