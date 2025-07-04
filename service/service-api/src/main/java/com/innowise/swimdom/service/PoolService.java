package com.innowise.swimdom.service;

import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service for work with pool.
 */
@Service
public interface PoolService {

    /**
     * Creates a new pool.
     *
     * @param pool the pool data to create
     * @return the created PoolDto with generated ID and details
     */
    PoolDto createPool(PoolDto pool);

    /**
     * Searches for pools matching the given criteria.
     *
     * @param pool the PoolDto containing search filters
     * @return list of PoolDto matching the search criteria
     */
    List<PoolDto> searchPools(PoolDto pool);

    /**
     * Updates an existing pool identified by its name.
     *
     * @param updatedPool the PoolDto with updated data
     * @return the updated PoolDto
     */
    PoolDto updatePool(PoolDto updatedPool);

    /**
     * Delete an existing pool identified by its name.
     *
     * @param poolId the PoolDto with updated data
     */
    void deletePool(UUID poolId);

    /**
     * Deletes the specified pool.
     *
     * @param poolDto the PoolDto representing the pool to delete
     */
    void deleteWorkingHours(PoolDto poolDto);

    /**
     * Creates or updates the schedule for a pool.
     *
     * @param poolDto the poolDto containing schedule details
     * @return the created or updated PoolWorkingHoursDto
     */
    Set<PoolWorkingHours> createOrUpdateWorkingHours(PoolDto poolDto);

    /**
     * Deletes the schedule entry for a pool on a specific day of the week.
     *
     * @param poolDto the PoolDto representing the pool
     */
    void deleteWorkingHoursByDay(PoolDto poolDto);

    /**
     * Retrieves all schedule entries for the specified pool.
     *
     * @param dto the PoolDto representing the pool
     * @return list of PoolWorkingHoursDto for the pool
     */
    Set<PoolWorkingHoursDto> getWorkingHoursForPool(PoolDto dto);

    /**
     * Retrieves all pools that have a schedule on the specified day of the week.
     *
     * @param dayOfWeek the day of the week (1 = Monday, 7 = Sunday)
     * @return list of PoolDto with schedules on the given day
     */
    List<PoolDto> getPoolsByDayOfWeek(Short dayOfWeek);

}

