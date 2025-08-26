package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.PoolWorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Repository to get Pool work hours.
 */
@Repository
public interface PoolWorkingHoursRepository extends JpaRepository<PoolWorkingHours, UUID> {

    /**
     * Delete working hours for the pool on a specific day of the week.
     */
    void deleteAllByPoolId(UUID poolId);

    /**
     * Finds the working hours for the pool on a specific day of the week.
     */
    Optional<PoolWorkingHours> findByPoolIdAndWeekday(UUID poolId, Short weekday);

    /**
     * Finds all the working hours for the pool.
     */
    Set<PoolWorkingHours> findByPoolId(UUID poolId);
}
