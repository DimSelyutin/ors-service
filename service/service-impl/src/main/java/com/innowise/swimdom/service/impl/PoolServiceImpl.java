package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.exception.PoolNotFoundException;
import com.innowise.swimdom.mapper.PoolMapper;
import com.innowise.swimdom.mapper.PoolWorkingHoursMapper;
import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import com.innowise.swimdom.repository.PoolRepository;
import com.innowise.swimdom.repository.PoolWorkingHoursRepository;
import com.innowise.swimdom.repository.specification.PoolSpecification;
import com.innowise.swimdom.service.PoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service implementation for PoolService.
 */
@RequiredArgsConstructor
@Slf4j
public class PoolServiceImpl implements PoolService {

    private PoolRepository poolRepository;
    private PoolMapper poolMapper;
    private PoolWorkingHoursRepository poolWorkingHoursRepository;
    private PoolWorkingHoursMapper poolWorkingHoursMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PoolDto createPool(PoolDto poolDto) {
        log.debug("createPool - start, poolDto: {}", poolDto);
        Pool pool = poolMapper.toPool(poolDto);
        Pool savedPool = poolRepository.save(pool);
        log.debug("createPool - end, savedPool: {}", savedPool);
        return poolMapper.toPoolDto(savedPool);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoolDto> searchPools(PoolDto poolFilterDto) {
        log.debug("searchPools start, poolFilterDto: {}", poolFilterDto);
        Specification<Pool> spec = PoolSpecification.byFilter(poolFilterDto);
        List<Pool> pools = poolRepository.findAll(spec);
        return poolMapper.toPoolsDto(pools);
    }

    /**
     * {@inheritDoc}
     * `
     */
    @Override
    public PoolDto updatePool(PoolDto poolDto) {
        log.debug("updatePool - start, poolDto: {}", poolDto);
        return poolRepository.findPoolById(poolDto.getId())
            .map(existingPool -> {
                poolMapper.updatePoolFromDto(poolDto, existingPool);
                Pool updatedPool = poolRepository.save(existingPool);
                log.debug("updatePool - successful, updated pool: {}", updatedPool);
                return poolMapper.toPoolDto(updatedPool);
            })
            .orElseThrow(() -> {
                log.warn("updatePool - Pool with id {} not found", poolDto.getId());
                return new PoolNotFoundException("Pool not found with id: " + poolDto.getId());
            });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePoolById(UUID poolId) {
        log.debug("deletePool - start, poolDto: {}", poolId);

        poolRepository.existsById(poolId);
        poolRepository.deleteById(poolId);
        log.debug("deletePool - end, poolDto: {}", poolId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteWorkingHoursById(UUID poolId) {
        log.debug("deleteWorkingHours - start for poolId: {}", poolId);
        poolWorkingHoursRepository.deleteAllByPoolId(poolId);
        log.debug("deleteWorkingHours - end for poolId: {}", poolId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<PoolWorkingHours> createOrUpdateWorkingHours(PoolDto poolDto) {
        log.debug("createOrUpdateWorkingHours - start, dtoList size: {}", poolDto.getPoolWorkingHours().size());

        if (poolDto.getPoolWorkingHours().isEmpty()) {
            log.debug("createOrUpdateWorkingHours - empty dtoList, nothing to save");
            return Collections.emptyList();
        }
        UUID poolId = poolDto.getId();
        poolRepository.findPoolById(poolDto.getId())
            .orElseThrow(() -> new PoolNotFoundException("Pool not found with id " + poolId));
        deleteWorkingHoursById(poolId);

        List<PoolWorkingHours> newHours = poolWorkingHoursMapper.toPoolWorkingHoursList(poolDto.getPoolWorkingHours());

        log.debug("createOrUpdateWorkingHours - saving newHours size: {}", newHours.size());
        return poolWorkingHoursRepository.saveAll(newHours);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<PoolWorkingHoursDto> getWorkingHoursForPool(PoolDto dto) {
        log.debug("getSchedulesForPool - start, poolId: {}", dto.getId());
        Set<PoolWorkingHoursDto> poolWorkingHours = poolWorkingHoursMapper.toPoolWorkingHoursDtoList(
            poolWorkingHoursRepository.findByPoolId(dto.getId()));

        log.debug("getSchedulesForPool - found {} schedules", poolWorkingHours.size());
        return poolWorkingHours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoolDto> getPoolsByDayOfWeek(Short dayOfWeek) {
        log.debug("getPoolsByDayOfWeek - start, dayOfWeek: {}", dayOfWeek);
        List<PoolDto> pools = poolRepository.findPoolsByPoolWorkingHoursWeekday(dayOfWeek)
            .map(poolMapper::toPoolsDto)
            .orElseThrow(() -> {
                log.warn("getPoolsByDayOfWeek - Pool with weekday {} not found", dayOfWeek);
                return new PoolNotFoundException("PoolWorkingHoursDto not found with poolId: " + dayOfWeek);
            });
        log.debug("getPoolsByDayOfWeek - found {} pools", pools.size());
        return pools;
    }
}
