package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.PoolWorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

/**
 * Repository for pools.
 */
@Repository
public interface PoolWorkingHoursRepository extends JpaRepository<PoolWorkingHours, UUID> {

    Set<PoolWorkingHours> findByPoolId(UUID poolId);

    void deleteAllByPoolId(UUID poolId);
}
