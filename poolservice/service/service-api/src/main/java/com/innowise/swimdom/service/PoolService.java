package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service for work with pool.
 **/
public interface PoolService {

    PoolDto createPool(PoolDto pool);

    List<PoolDto> searchPools(PoolDto pool);

    PoolDto updatePool(PoolDto updatedPool);

    void deletePoolById(UUID poolId);

    void deleteWorkingHoursById(UUID poolId);

    List<PoolWorkingHoursDto> createOrUpdateWorkingHours(PoolDto poolDto);

    Set<PoolWorkingHoursDto> getWorkingHoursForPool(UUID poolId);
}


