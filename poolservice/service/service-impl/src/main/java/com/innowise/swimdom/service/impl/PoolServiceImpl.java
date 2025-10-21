package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.event.PoolCreatedEvent;
import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.mapper.PoolMapper;
import com.innowise.swimdom.mapper.PoolWorkingHoursMapper;
import com.innowise.swimdom.repository.PoolRepository;
import com.innowise.swimdom.repository.PoolWorkingHoursRepository;
import com.innowise.swimdom.service.PoolService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service for work with pools entity.
 */
@Service
public class PoolServiceImpl implements PoolService {

    private final PoolRepository poolRepository;
    private final PoolMapper poolMapper;
    private final PoolWorkingHoursRepository poolWorkingHoursRepository;
    private final PoolWorkingHoursMapper poolWorkingHoursMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String poolEventsTopic;

    public PoolServiceImpl(PoolRepository poolRepository,
                           PoolMapper poolMapper,
                           PoolWorkingHoursRepository poolWorkingHoursRepository,
                           PoolWorkingHoursMapper poolWorkingHoursMapper,
                           KafkaTemplate<String, Object> kafkaTemplate,
                           @Value("${topics.pool-events:pool.events}") String poolEventsTopic) {
        this.poolRepository = poolRepository;
        this.poolMapper = poolMapper;
        this.poolWorkingHoursRepository = poolWorkingHoursRepository;
        this.poolWorkingHoursMapper = poolWorkingHoursMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.poolEventsTopic = poolEventsTopic;
    }

    @Override
    public PoolDto createPool(PoolDto poolDto) {
        Pool pool = poolMapper.toPool(poolDto);
        Pool savedPool = poolRepository.save(pool);
        kafkaTemplate.send(poolEventsTopic, savedPool.getId().toString(),
            new PoolCreatedEvent(savedPool.getId(), savedPool.getName(), savedPool.getLocation()));
        return poolMapper.toPoolDto(savedPool);
    }

    @Override
    public List<PoolDto> searchPools(PoolDto poolFilterDto) {
        // TODO: add specification-based filter if needed
        return poolRepository.findAll().stream().map(poolMapper::toPoolDto).toList();
    }

    @Override
    public PoolDto updatePool(PoolDto poolDto) {
        return poolRepository.findPoolById(poolDto.getId())
                .map(existingPool -> {
                    poolMapper.updatePoolFromDto(poolDto, existingPool);
                    Pool updatedPool = poolRepository.save(existingPool);
                    return poolMapper.toPoolDto(updatedPool);
                })
                .orElseThrow(() -> new IllegalArgumentException("Pool not found with id: "
                    + poolDto.getId()));
    }

    @Override
    public void deletePoolById(UUID poolId) {
        poolRepository.deleteById(poolId);
    }

    @Override
    public void deleteWorkingHoursById(UUID poolId) {
        poolWorkingHoursRepository.deleteAllByPoolId(poolId);
    }

    @Transactional
    @Override
    public List<PoolWorkingHoursDto> createOrUpdateWorkingHours(PoolDto poolDto) {
        if (poolDto.getPoolWorkingHours() == null || poolDto.getPoolWorkingHours().isEmpty()) {
            return Collections.emptyList();
        }
        UUID poolId = poolDto.getId();
        poolRepository.findPoolById(poolId).orElseThrow(
            () -> new IllegalArgumentException("Pool not found with id " + poolId));
        deleteWorkingHoursById(poolId);

        List<PoolWorkingHours> newHours = poolWorkingHoursMapper.toPoolWorkingHoursList(
            poolDto.getPoolWorkingHours());
        return poolWorkingHoursRepository.saveAll(newHours).stream().map(
            poolWorkingHoursMapper::toPoolWorkingHoursDto).toList();
    }

    @Override
    public Set<PoolWorkingHoursDto> getWorkingHoursForPool(UUID poolId) {
        return poolWorkingHoursMapper.toPoolWorkingHoursDtoList(poolWorkingHoursRepository
            .findPoolById(poolId));
    }
}


