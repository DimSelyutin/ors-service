package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for entity {@link PoolWorkingHours}.
 *
 * @author DimSelyutin
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PoolWorkingHoursMapper {

    /**
     * Mapping PoolWorkingHoursDto to PoolWorkingHours entity.
     * Creat poolId in object with id.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "pool", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    PoolWorkingHours toPoolWorkingHours(PoolWorkingHoursDto dto);

    /**
     * Mapping PoolWorkingHours entity to PoolWorkingHoursDto.
     * Extracts poolId in field poolId.
     */
    @Mapping(target = "poolId", source = "entity.pool.id")
    PoolWorkingHoursDto toPoolWorkingHoursDto(PoolWorkingHours entity);

    /**
     * Mapping list of PoolWorkingHoursDto to list of PoolWorkingHours.
     */
    List<PoolWorkingHours> toPoolWorkingHoursList(Set<PoolWorkingHoursDto> dto);

    /**
     * Mapping list of PoolWorkingHours to list of PoolWorkingHoursDto.
     */
    Set<PoolWorkingHoursDto> toPoolWorkingHoursDtoList(Set<PoolWorkingHours> entities);

    /**
     * Update pool hours from dto.
     */
    @Mapping(source = "poolId", target = "entity.pool.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updatePoolWorkingHoursFromDto(PoolWorkingHoursDto dto, @MappingTarget PoolWorkingHours entity);
    
}
