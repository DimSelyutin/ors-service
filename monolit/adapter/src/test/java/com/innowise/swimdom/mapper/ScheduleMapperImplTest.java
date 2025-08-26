package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.openapi.model.ScheduleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ScheduleMapperImplTest {

    private ScheduleMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new ScheduleMapperImpl();
    }

    @Test
    void toSchedule_NullInput_ReturnsNull() {
        assertThat(mapper.toSchedule(null)).isNull();
    }

    @Test
    void toSchedule_ValidInput_MapsFieldsCorrectly() {
        UUID poolId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2025, 8, 5, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 5, 13, 0);

        ScheduleDto dto = new ScheduleDto()
            .poolId(poolId)
            .startDatetime(start)
            .endDatetime(end);

        Schedule schedule = mapper.toSchedule(dto);

        assertThat(schedule).isNotNull();
        assertThat(schedule.getPool()).isNotNull();
        assertThat(schedule.getPool().getId()).isEqualTo(poolId);
        assertThat(schedule.getStartDatetime()).isEqualTo(start);
        assertThat(schedule.getEndDatetime()).isEqualTo(end);
        assertThat(schedule.getUpdatedAt()).isNotNull();
        // Не проверяем точное время updatedAt, т.к. оно ставится внутри метода
    }

    @Test
    void toScheduleDto_NullInput_ReturnsNull() {
        assertThat(mapper.toScheduleDto(null)).isNull();
    }

    @Test
    void toScheduleDto_ValidInput_MapsFieldsCorrectly() {
        UUID id = UUID.randomUUID();
        UUID poolId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2025, 8, 5, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 5, 13, 0);
        LocalDateTime createdAt = LocalDateTime.of(2025, 8, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 8, 4, 15, 0);

        Pool pool = new Pool();
        pool.setId(poolId);

        Schedule schedule = new Schedule();
        schedule.setId(id);
        schedule.setPool(pool);
        schedule.setStartDatetime(start);
        schedule.setEndDatetime(end);
        schedule.setCreatedAt(createdAt);
        schedule.setUpdatedAt(updatedAt);

        ScheduleDto dto = mapper.toScheduleDto(schedule);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getPoolId()).isEqualTo(poolId);
        assertThat(dto.getStartDatetime()).isEqualTo(start);
        assertThat(dto.getEndDatetime()).isEqualTo(end);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void toScheduleDto_PoolIsNull_PoolIdIsNull() {
        Schedule schedule = new Schedule();
        schedule.setPool(null);

        ScheduleDto dto = mapper.toScheduleDto(schedule);

        assertThat(dto).isNotNull();
        assertThat(dto.getPoolId()).isNull();
    }

    @Test
    void toSchedules_NullInput_ReturnsNull() {
        assertThat(mapper.toSchedules(null)).isNull();
    }

    @Test
    void toSchedules_ValidInput_MapsListCorrectly() {
        ScheduleDto dto1 = new ScheduleDto();
        ScheduleDto dto2 = new ScheduleDto();

        List<Schedule> schedules = mapper.toSchedules(List.of(dto1, dto2));

        assertThat(schedules).hasSize(2);
        assertThat(schedules.get(0)).isNotNull();
        assertThat(schedules.get(1)).isNotNull();
    }

    @Test
    void toSchedulesDto_NullInput_ReturnsNull() {
        assertThat(mapper.toSchedulesDto(null)).isNull();
    }

    @Test
    void toSchedulesDto_ValidInput_MapsListCorrectly() {
        Schedule schedule1 = new Schedule();
        Schedule schedule2 = new Schedule();

        List<ScheduleDto> dtos = mapper.toSchedulesDto(List.of(schedule1, schedule2));

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0)).isNotNull();
        assertThat(dtos.get(1)).isNotNull();
    }

    @Test
    void updateScheduleFromDto_NullDto_DoesNothing() {
        Schedule existing = new Schedule();
        existing.setPool(new Pool());
        existing.setStartDatetime(LocalDateTime.now());
        existing.setEndDatetime(LocalDateTime.now());

        mapper.updateScheduleFromDto(null, existing);

        // Проверяем, что ничего не изменилось (по крайней мере, pool не стал null)
        assertThat(existing.getPool()).isNotNull();
    }

    @Test
    void updateScheduleFromDto_ValidDto_UpdatesFields() {
        UUID poolIdOld = UUID.randomUUID();
        UUID poolIdNew = UUID.randomUUID();

        Pool pool = new Pool();
        pool.setId(poolIdOld);

        LocalDateTime oldStart = LocalDateTime.of(2025, 8, 1, 10, 0);
        LocalDateTime oldEnd = LocalDateTime.of(2025, 8, 1, 11, 0);

        Schedule existing = new Schedule();
        existing.setPool(pool);
        existing.setStartDatetime(oldStart);
        existing.setEndDatetime(oldEnd);

        LocalDateTime newStart = LocalDateTime.of(2025, 8, 5, 12, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 8, 5, 13, 0);

        ScheduleDto dto = new ScheduleDto()
            .poolId(poolIdNew)
            .startDatetime(newStart)
            .endDatetime(newEnd);

        mapper.updateScheduleFromDto(dto, existing);

        assertThat(existing.getPool()).isNotNull();
        assertThat(existing.getPool().getId()).isEqualTo(poolIdNew);
        assertThat(existing.getStartDatetime()).isEqualTo(newStart);
        assertThat(existing.getEndDatetime()).isEqualTo(newEnd);
        assertThat(existing.getUpdatedAt()).isNotNull();
    }

    @Test
    void updateScheduleFromDto_ExistingPoolIsNull_CreatesNewPoolAndUpdates() {
        Schedule existing = new Schedule();
        existing.setPool(null);

        UUID newPoolId = UUID.randomUUID();
        LocalDateTime newStart = LocalDateTime.of(2025, 8, 5, 12, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 8, 5, 13, 0);

        ScheduleDto dto = new ScheduleDto()
            .poolId(newPoolId)
            .startDatetime(newStart)
            .endDatetime(newEnd);

        mapper.updateScheduleFromDto(dto, existing);

        assertThat(existing.getPool()).isNotNull();
        assertThat(existing.getPool().getId()).isEqualTo(newPoolId);
        assertThat(existing.getStartDatetime()).isEqualTo(newStart);
        assertThat(existing.getEndDatetime()).isEqualTo(newEnd);
        assertThat(existing.getUpdatedAt()).isNotNull();
    }
}

