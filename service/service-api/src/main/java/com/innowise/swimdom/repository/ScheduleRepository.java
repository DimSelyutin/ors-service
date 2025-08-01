package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for schedule.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    /**
     * Finds all pool schedules in the specified time range.
     */
    List<Schedule> findByPoolIdAndStartDatetimeBetween(UUID poolId, LocalDateTime from, LocalDateTime to);

    /**
     * Finds all pool schedules.
     */
    List<Schedule> findByPoolId(UUID poolId);
}
