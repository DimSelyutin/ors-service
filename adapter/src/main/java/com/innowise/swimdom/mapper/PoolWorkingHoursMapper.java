package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for entity {@link PoolWorkingHours}.
 *
 * @author DimSelyutin
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PoolWorkingHoursMapper {

    /**
     * Mapping PoolWorkingHoursDto to PoolWorkingHours entity.
     * Creat poolId in object with id.
     */
    PoolWorkingHours toPoolWorkingHours(PoolWorkingHoursDto dto);

    /**
     * Mapping PoolWorkingHours entity to PoolWorkingHoursDto.
     * Extracts pool.id in field poolId.
     */
    PoolWorkingHoursDto toPoolWorkingHoursDto(PoolWorkingHours entity);

    /**
     * Mapping list of PoolWorkingHoursDto to list of PoolWorkingHours.
     */
    Set<PoolWorkingHours> toPoolWorkingHoursList(Set<PoolWorkingHoursDto> dto);

    /**
     * Mapping list of PoolWorkingHours to list of PoolWorkingHoursDto.
     */
    Set<PoolWorkingHoursDto> toPoolWorkingHoursDtoList(Set<PoolWorkingHours> entities);

    /**
     * Update pool hours from dto.
     */
    void updatePoolWorkingHoursFromDto(PoolWorkingHoursDto dto, @MappingTarget PoolWorkingHours entity);
    
}
