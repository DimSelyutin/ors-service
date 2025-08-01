package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.exception.PoolNotFoundException;
import com.innowise.swimdom.exception.ScheduleNotFoundException;
import com.innowise.swimdom.mapper.ScheduleMapper;
import com.innowise.swimdom.openapi.model.ScheduleDto;
import com.innowise.swimdom.repository.PoolRepository;
import com.innowise.swimdom.repository.ScheduleRepository;
import com.innowise.swimdom.service.util.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.innowise.swimdom.service.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private PoolRepository poolRepository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;


    @Test
    void createSchedule_poolNotFound_throwsException() {
        // GIVEN
        ScheduleDto testScheduleDto = TestData.testScheduleDto;
        when(poolRepository.findById(testScheduleDto.getPoolId())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(PoolNotFoundException.class, () -> scheduleService.createSchedule(testScheduleDto));
        verify(poolRepository, times(1)).findById(testScheduleDto.getPoolId());
        verify(scheduleRepository, never()).save(any());
    }


    @Test
    void createSchedule_startTimeAfterEndTime_throwsException() {
        // GIVEN
        ScheduleDto testScheduleDto = TestData.testScheduleDto;
        testScheduleDto.setStartDatetime(SCHEDULE_END_DATETIME);
        testScheduleDto.setEndDatetime(SCHEDULE_START_DATETIME);
        Pool poolEntity = testPoolEntity;

        when(poolRepository.findById(testScheduleDto.getPoolId())).thenReturn(Optional.of(poolEntity));

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> scheduleService.createSchedule(testScheduleDto));
        verify(poolRepository, times(1)).findById(testScheduleDto.getPoolId());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void getSchedule_success() {
        // GIVEN
        String scheduleId = SCHEDULE_ID.toString();
        Schedule scheduleEntity = testScheduleEntity;
        ScheduleDto scheduleDto = testScheduleDto;

        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleMapper.toScheduleDto(scheduleEntity)).thenReturn(scheduleDto);

        // WHEN
        ScheduleDto result = scheduleService.getSchedule(scheduleId);

        // THEN
        assertNotNull(result);
        assertEquals(scheduleDto, result);
        verify(scheduleRepository, times(1)).findById(SCHEDULE_ID);
        verify(scheduleMapper, times(1)).toScheduleDto(scheduleEntity);
    }

    @Test
    void getSchedule_invalidId_throwsException() {
        // GIVEN
        String invalidId = "invalid-uuid";

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> scheduleService.getSchedule(invalidId));
        verify(scheduleRepository, never()).findById(any());
    }

    @Test
    void getSchedule_notFound_returnsNull() {
        // GIVEN
        String scheduleId = SCHEDULE_ID.toString();
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.empty());

        // WHEN
        ScheduleDto result = scheduleService.getSchedule(scheduleId);

        // THEN
        assertNull(result);
        verify(scheduleRepository, times(1)).findById(SCHEDULE_ID);
        verify(scheduleMapper, never()).toScheduleDto(any());
    }

    @Test
    void getAllSchedules_success() {
        // GIVEN
        List<Schedule> schedules = List.of(testScheduleEntity);
        List<ScheduleDto> scheduleDtos = List.of(testScheduleDto);

        when(scheduleRepository.findAll()).thenReturn(schedules);
        when(scheduleMapper.toScheduleDto(testScheduleEntity)).thenReturn(testScheduleDto);

        // WHEN
        List<ScheduleDto> result = scheduleService.getAllSchedules();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testScheduleDto, result.get(0));
        verify(scheduleRepository, times(1)).findAll();
        verify(scheduleMapper, times(1)).toScheduleDto(testScheduleEntity);
    }

    @Test
    void getSchedulesByPool_success() {
        // GIVEN
        UUID poolId = POOL_ID;
        List<Schedule> schedules = List.of(testScheduleEntity);
        List<ScheduleDto> scheduleDtos = List.of(testScheduleDto);

        when(poolRepository.findById(poolId)).thenReturn(Optional.of(testPoolEntity));
        when(scheduleRepository.findByPoolId(poolId)).thenReturn(schedules);
        when(scheduleMapper.toScheduleDto(testScheduleEntity)).thenReturn(testScheduleDto);

        // WHEN
        List<ScheduleDto> result = scheduleService.getSchedulesByPool(poolId);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testScheduleDto, result.get(0));
        verify(poolRepository, times(1)).findById(poolId);
        verify(scheduleRepository, times(1)).findByPoolId(poolId);
        verify(scheduleMapper, times(1)).toScheduleDto(testScheduleEntity);
    }

    @Test
    void getSchedulesByPool_poolNotFound_throwsException() {
        // GIVEN
        UUID poolId = POOL_ID;
        when(poolRepository.findById(poolId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(PoolNotFoundException.class, () -> scheduleService.getSchedulesByPool(poolId));
        verify(poolRepository, times(1)).findById(poolId);
        verify(scheduleRepository, never()).findByPoolId(any());
    }

    @Test
    void deleteSchedule_success() {
        // GIVEN
        UUID scheduleId = SCHEDULE_ID;
        Schedule scheduleEntity = testScheduleEntity;

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

        // WHEN
        scheduleService.deleteSchedule(scheduleId);

        // THEN
        verify(scheduleRepository, times(1)).findById(scheduleId);
        verify(scheduleRepository, times(1)).delete(scheduleEntity);
    }

    @Test
    void deleteSchedule_notFound_throwsException() {
        // GIVEN
        UUID scheduleId = SCHEDULE_ID;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ScheduleNotFoundException.class, () -> scheduleService.deleteSchedule(scheduleId));
        verify(scheduleRepository, times(1)).findById(scheduleId);
        verify(scheduleRepository, never()).delete(any());
    }

    @Test
    void getSchedulesInRange_success() {
        // GIVEN
        LocalDateTime from = SCHEDULE_START_DATETIME.minusHours(1);
        LocalDateTime to = SCHEDULE_END_DATETIME.plusHours(1);
        List<Schedule> schedules = List.of(testScheduleEntity);
        List<ScheduleDto> scheduleDtos = List.of(testScheduleDto);

        when(scheduleRepository.findByStartDatetimeBetween(from, to)).thenReturn(schedules);
        when(scheduleMapper.toScheduleDto(testScheduleEntity)).thenReturn(testScheduleDto);

        // WHEN
        List<ScheduleDto> result = scheduleService.getSchedulesInRange(from, to);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testScheduleDto, result.get(0));
        verify(scheduleRepository, times(1)).findByStartDatetimeBetween(from, to);
        verify(scheduleMapper, times(1)).toScheduleDto(testScheduleEntity);
    }

    @Test
    void getSchedulesInRange_nullDates_throwsException() {
        // WHEN & THEN
        assertThrows(IllegalArgumentException.class,
            () -> scheduleService.getSchedulesInRange(null, SCHEDULE_END_DATETIME));
        assertThrows(IllegalArgumentException.class,
            () -> scheduleService.getSchedulesInRange(SCHEDULE_START_DATETIME, null));
        verify(scheduleRepository, never()).findByStartDatetimeBetween(any(), any());
    }

    @Test
    void getSchedulesInRange_invalidRange_throwsException() {
        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () ->
            scheduleService.getSchedulesInRange(SCHEDULE_END_DATETIME, SCHEDULE_START_DATETIME));
        verify(scheduleRepository, never()).findByStartDatetimeBetween(any(), any());
    }

    @Test
    void getSchedulesByPoolInRange_success() {
        // GIVEN
        UUID poolId = POOL_ID;
        LocalDateTime from = SCHEDULE_START_DATETIME.minusHours(1);
        LocalDateTime to = SCHEDULE_END_DATETIME.plusHours(1);
        List<Schedule> schedules = List.of(testScheduleEntity);
        List<ScheduleDto> scheduleDtos = List.of(testScheduleDto);

        when(poolRepository.findById(poolId)).thenReturn(Optional.of(testPoolEntity));
        when(scheduleRepository.findByPoolIdAndStartDatetimeBetween(poolId, from, to)).thenReturn(schedules);
        when(scheduleMapper.toScheduleDto(testScheduleEntity)).thenReturn(testScheduleDto);

        // WHEN
        List<ScheduleDto> result = scheduleService.getSchedulesByPoolInRange(poolId, from, to);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testScheduleDto, result.get(0));
        verify(poolRepository, times(1)).findById(poolId);
        verify(scheduleRepository, times(1)).findByPoolIdAndStartDatetimeBetween(poolId, from, to);
        verify(scheduleMapper, times(1)).toScheduleDto(testScheduleEntity);
    }

    @Test
    void updateSchedule_success() {
        // GIVEN
        ScheduleDto testScheduleDto = TestData.testScheduleDto;
        Schedule existingSchedule = testScheduleEntity;
        Pool poolEntity = testPoolEntity;

        when(scheduleRepository.findById(testScheduleDto.getId())).thenReturn(Optional.of(existingSchedule));
        when(poolRepository.findById(testScheduleDto.getPoolId())).thenReturn(Optional.of(poolEntity));
        when(scheduleRepository.findByPoolIdAndStartDatetimeBetween(
            eq(testScheduleDto.getPoolId()), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of());
        when(scheduleRepository.save(existingSchedule)).thenReturn(existingSchedule);
        when(scheduleMapper.toScheduleDto(existingSchedule)).thenReturn(testScheduleDto);

        // WHEN
        ScheduleDto result = scheduleService.updateSchedule(testScheduleDto);

        // THEN
        assertNotNull(result);
        assertEquals(testScheduleDto, result);
        verify(scheduleRepository, times(1)).findById(testScheduleDto.getId());
        verify(poolRepository, times(1)).findById(testScheduleDto.getPoolId());
        verify(scheduleRepository, times(1)).save(existingSchedule);
        verify(scheduleMapper, times(1)).toScheduleDto(existingSchedule);
    }

    @Test
    void updateSchedule_scheduleNotFound_throwsException() {
        // GIVEN
        ScheduleDto testScheduleDto = TestData.testScheduleDto;
        when(scheduleRepository.findById(testScheduleDto.getId())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ScheduleNotFoundException.class, () -> scheduleService.updateSchedule(testScheduleDto));
        verify(scheduleRepository, times(1)).findById(testScheduleDto.getId());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void isTimeSlotAvailable_available_returnsTrue() {
        // GIVEN
        UUID poolId = POOL_ID;
        LocalDateTime startTime = SCHEDULE_START_DATETIME;
        LocalDateTime endTime = SCHEDULE_END_DATETIME;

        when(scheduleRepository.findByPoolIdAndStartDatetimeBetween(
            eq(poolId), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of());

        // WHEN
        boolean result = scheduleService.isTimeSlotAvailable(poolId, startTime, endTime);

        // THEN
        assertTrue(result);
        verify(scheduleRepository, times(1)).findByPoolIdAndStartDatetimeBetween(
            eq(poolId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void isTimeSlotAvailable_notAvailable_returnsFalse() {
        // GIVEN
        UUID poolId = POOL_ID;
        LocalDateTime startTime = SCHEDULE_START_DATETIME;
        LocalDateTime endTime = SCHEDULE_END_DATETIME;
        Schedule existingSchedule = testScheduleEntity;

        when(scheduleRepository.findByPoolIdAndStartDatetimeBetween(
            eq(poolId), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(existingSchedule));

        // WHEN
        boolean result = scheduleService.isTimeSlotAvailable(poolId, startTime, endTime);

        // THEN
        assertFalse(result);
        verify(scheduleRepository, times(1)).findByPoolIdAndStartDatetimeBetween(
            eq(poolId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void isTimeSlotAvailable_nullTimes_returnsFalse() {
        // WHEN
        boolean result1 = scheduleService.isTimeSlotAvailable(POOL_ID, null, SCHEDULE_END_DATETIME);
        boolean result2 = scheduleService.isTimeSlotAvailable(POOL_ID, SCHEDULE_START_DATETIME, null);

        // THEN
        assertFalse(result1);
        assertFalse(result2);
        verify(scheduleRepository, never()).findByPoolIdAndStartDatetimeBetween(any(), any(), any());
    }

    @Test
    void isTimeSlotAvailable_startAfterEnd_returnsFalse() {
        // WHEN
        boolean result = scheduleService.isTimeSlotAvailable(POOL_ID, SCHEDULE_END_DATETIME, SCHEDULE_START_DATETIME);

        // THEN
        assertFalse(result);
        verify(scheduleRepository, never()).findByPoolIdAndStartDatetimeBetween(any(), any(), any());
    }
} 