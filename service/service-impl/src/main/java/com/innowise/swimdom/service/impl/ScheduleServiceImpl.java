package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.exception.PoolNotFoundException;
import com.innowise.swimdom.exception.ScheduleNotFoundException;
import com.innowise.swimdom.mapper.ScheduleMapper;
import com.innowise.swimdom.openapi.model.ScheduleDto;
import com.innowise.swimdom.repository.PoolRepository;
import com.innowise.swimdom.repository.ScheduleRepository;
import com.innowise.swimdom.service.ScheduleService;
import com.innowise.swimdom.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation for ScheduleService.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PoolRepository poolRepository;
    private final ScheduleMapper scheduleMapper;

    /**
     * Creates a new schedule for the pool.
     * Validates pool existence and time slot availability.
     */
    @Override
    @Transactional
    public ScheduleDto createSchedule(ScheduleDto scheduleDTO) {
        // Validate pool exists
        Pool pool = poolRepository.findById(scheduleDTO.getPoolId())
            .orElseThrow(() -> new PoolNotFoundException(Constants.POOL_NOT_FOUND + scheduleDTO.getPoolId()));

        // Validate time slot availability
        if (!isTimeSlotAvailable(scheduleDTO.getPoolId(), scheduleDTO.getStartDatetime(),
            scheduleDTO.getEndDatetime())) {
            throw new IllegalArgumentException("The selected time slot is not available.");
        }

        // Validate start time is before end time
        if (scheduleDTO.getStartDatetime().isAfter(scheduleDTO.getEndDatetime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        Schedule schedule = scheduleMapper.toSchedule(scheduleDTO);
        schedule.setPool(pool);

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toScheduleDto(savedSchedule);
    }

    /**
     * Retrieves a schedule by its unique ID.
     */
    @Override
    public ScheduleDto getSchedule(String scheduleId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(scheduleId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid schedule ID format.");
        }
        return scheduleRepository.findById(uuid)
            .map(scheduleMapper::toScheduleDto)
            .orElse(null);
    }

    /**
     * Retrieves all schedules.
     */
    @Override
    public List<ScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
            .map(scheduleMapper::toScheduleDto)
            .toList();
    }

    /**
     * Retrieves all schedules for a given pool.
     */
    @Override
    public List<ScheduleDto> getSchedulesByPool(UUID poolId) {
        // Validate pool exists
        poolRepository.findById(poolId)
            .orElseThrow(() -> new PoolNotFoundException(Constants.POOL_NOT_FOUND + poolId));

        return scheduleRepository.findByPoolId(poolId).stream()
            .map(scheduleMapper::toScheduleDto)
            .toList();
    }

    /**
     * Deletes a schedule by its ID.
     */
    @Override
    @Transactional
    public void deleteSchedule(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found with ID: " + scheduleId));
        scheduleRepository.delete(schedule);
    }

    /**
     * Retrieves all schedules within a specified time range.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDto> getSchedulesInRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From and To dates cannot be null.");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        return scheduleRepository.findByStartDatetimeBetween(from, to).stream()
            .map(scheduleMapper::toScheduleDto)
            .toList();
    }

    /**
     * Retrieves all schedules for a pool within a specified time range.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDto> getSchedulesByPoolInRange(UUID poolId, LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From and To dates cannot be null.");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        // Validate pool exists
        poolRepository.findById(poolId)
            .orElseThrow(() -> new PoolNotFoundException(Constants.POOL_NOT_FOUND + poolId));

        return scheduleRepository.findByPoolIdAndStartDatetimeBetween(poolId, from, to).stream()
            .map(scheduleMapper::toScheduleDto)
            .toList();
    }

    /**
     * Updates a schedule.
     * Validates pool existence and time slot availability.
     */
    @Override
    @Transactional
    public ScheduleDto updateSchedule(ScheduleDto scheduleDTO) {
        UUID scheduleId = scheduleDTO.getId();
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found with ID: " + scheduleId));

        if (!isTimeSlotAvailableForUpdate(scheduleId, scheduleDTO.getPoolId(),
            scheduleDTO.getStartDatetime(), scheduleDTO.getEndDatetime())) {
            throw new IllegalArgumentException("The selected time slot is not available.");
        }

        // Validate start time is before end time
        if (scheduleDTO.getStartDatetime().isAfter(scheduleDTO.getEndDatetime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        // Validate pool exists
        Pool pool = poolRepository.findById(scheduleDTO.getPoolId())
            .orElseThrow(() -> new PoolNotFoundException(Constants.POOL_NOT_FOUND + scheduleDTO.getPoolId()));

        // Update schedule fields
        scheduleMapper.updateScheduleFromDto(scheduleDTO, existingSchedule);
        existingSchedule.setPool(pool);

        Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
        return scheduleMapper.toScheduleDto(updatedSchedule);
    }

    /**
     * Checks if a time slot is available for scheduling.
     */
    @Override
    public boolean isTimeSlotAvailable(UUID poolId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        log.info("timing22:{} - {}", startTime, endTime);
        if (startTime.isAfter(endTime)) {
            return false;
        }
        log.info("timing22:{}", startTime);

        // Check for overlapping schedules
        List<Schedule> overlappingSchedules = scheduleRepository.findByPoolIdAndStartDatetimeBetween(
            poolId, startTime.minusMinutes(1), endTime.plusMinutes(1));
        log.debug("info:{}", overlappingSchedules);

        return overlappingSchedules.isEmpty();
    }

    /**
     * Checks if a time slot is available for updating a schedule (excluding the current schedule).
     */
    private boolean isTimeSlotAvailableForUpdate(UUID scheduleId, UUID poolId,
                                                 LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }

        if (startTime.isAfter(endTime)) {
            return false;
        }

        // Check for overlapping schedules (excluding current schedule)
        List<Schedule> overlappingSchedules = scheduleRepository.findByPoolIdAndStartDatetimeBetween(
            poolId, startTime.minusMinutes(1), endTime.plusMinutes(1));

        return overlappingSchedules.stream()
            .noneMatch(schedule -> !schedule.getId().equals(scheduleId));
    }
} 
