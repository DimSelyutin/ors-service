package com.innowise.swimdom.mapper;

import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import com.innowise.swimdom.entity.PoolWorkingHours;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for pool entity.
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PoolWorkingHoursMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "pool", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    PoolWorkingHours toPoolWorkingHours(PoolWorkingHoursDto dto);

    @Mapping(target = "poolId", source = "entity.pool.id")
    PoolWorkingHoursDto toPoolWorkingHoursDto(PoolWorkingHours entity);

    List<PoolWorkingHours> toPoolWorkingHoursList(Set<PoolWorkingHoursDto> dto);

    Set<PoolWorkingHoursDto> toPoolWorkingHoursDtoList(Set<PoolWorkingHours> entities);
}


