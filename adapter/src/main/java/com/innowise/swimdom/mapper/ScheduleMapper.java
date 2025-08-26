package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.openapi.model.ScheduleDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for entity {@link com.innowise.swimdom.entity.Schedule}.
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ScheduleMapper {

    /**
     * Mapping scheduleDto to Schedule.
     *
     * @param scheduleDto object
     * @return Schedule
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pool.id", source = "poolId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Schedule toSchedule(ScheduleDto scheduleDto);

    /**
     * Mapping schedule to ScheduleDto.
     *
     * @param schedule object
     * @return ScheduleDto
     */
    @Mapping(target = "poolId", source = "pool.id")
    ScheduleDto toScheduleDto(Schedule schedule);

    /**
     * Mapping List scheduleDtos to List Schedule.
     *
     * @param schedulesDto objects
     * @return List Schedule
     */
    List<Schedule> toSchedules(List<ScheduleDto> schedulesDto);

    /**
     * Mapping List schedules to List ScheduleDto.
     *
     * @param schedules objects
     * @return List ScheduleDto
     */
    List<ScheduleDto> toSchedulesDto(List<Schedule> schedules);

    /**
     * Mapping scheduleDto to existing Schedule.
     *
     * @param scheduleDto and existingSchedule objects
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pool.id", source = "poolId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateScheduleFromDto(ScheduleDto scheduleDto, @MappingTarget Schedule existingSchedule);
}
