package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.ScheduleDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Schedule management service for pool schedules.
 * Provides methods to create, retrieve, update, and delete schedules.
 */
@Service
public interface ScheduleService {

    /**
     * Creates a new schedule for the pool.
     *
     * @param scheduleDTO the schedule data transfer object containing schedule details
     * @return the created schedule details
     */
    ScheduleDto createSchedule(ScheduleDto scheduleDTO);

    /**
     * Retrieves a schedule by its unique ID.
     *
     * @param scheduleId the ID of the schedule
     * @return the schedule details or null if not found
     */
    ScheduleDto getSchedule(String scheduleId);

    /**
     * Retrieves all schedules.
     *
     * @return list of all schedules
     */
    List<ScheduleDto> getAllSchedules();

    /**
     * Retrieves all schedules for a given pool.
     *
     * @param poolId the ID of the pool
     * @return list of schedules associated with the pool
     */
    List<ScheduleDto> getSchedulesByPool(UUID poolId);

    /**
     * Deletes a schedule by its ID.
     *
     * @param scheduleId the ID of the schedule to delete
     */
    void deleteSchedule(UUID scheduleId);

    /**
     * Retrieves all schedules within a specified time range.
     *
     * @param from start of the time range (inclusive)
     * @param to   end of the time range (exclusive)
     * @return list of schedules within the specified time range
     */
    List<ScheduleDto> getSchedulesInRange(LocalDateTime from, LocalDateTime to);

    /**
     * Retrieves all schedules for a pool within a specified time range.
     *
     * @param poolId the ID of the pool
     * @param from   start of the time range (inclusive)
     * @param to     end of the time range (exclusive)
     * @return list of schedules for the pool within the specified time range
     */
    List<ScheduleDto> getSchedulesByPoolInRange(UUID poolId, LocalDateTime from, LocalDateTime to);

    /**
     * Updates a schedule.
     *
     * @param scheduleDTO the updated schedule data
     * @return the updated schedule details
     */
    ScheduleDto updateSchedule(ScheduleDto scheduleDTO);

    /**
     * Checks if a time slot is available for scheduling.
     *
     * @param poolId    the ID of the pool
     * @param startTime the start time of the time slot
     * @param endTime   the end time of the time slot
     * @return true if the time slot is available, false if already scheduled
     */
    boolean isTimeSlotAvailable(UUID poolId, LocalDateTime startTime, LocalDateTime endTime);
}
