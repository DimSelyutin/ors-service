package com.innowise.swimdom.mapper;

import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.entity.Pool;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for pool entity.
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, uses = { PoolWorkingHoursMapper.class })
public interface PoolMapper {

    @Mapping(target = "id", source = "id")
    Pool toPool(PoolDto dto);

    PoolDto toPoolDto(Pool entity);

    List<PoolDto> toPoolsDto(List<Pool> pools);

    void updatePoolFromDto(PoolDto dto, @MappingTarget Pool entity);
}


