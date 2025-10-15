package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.openapi.model.PoolDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for entity {@link com.innowise.swimdom.entity.Pool}.
 *
 * @author DimSelyutin
 */
@Mapper(componentModel = SPRING, uses = PoolWorkingHoursMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PoolMapper {

    /**
     * Mapping poolDto to Pool.
     *
     * @param poolDto object
     * @return Pool
     */
    @Mapping(target = "id", ignore = true)
    Pool toPool(PoolDto poolDto);

    /**
     * Mapping pool to PoolDto.
     *
     * @param pool object
     * @return PoolDto
     */
    PoolDto toPoolDto(Pool pool);

    /**
     * Mapping List poolDtos to List Pool.
     *
     * @param poolsDto objects
     * @return List Pool
     */
    List<Pool> toPools(List<PoolDto> poolsDto);

    /**
     * Mapping List pools to List PoolDto.
     *
     * @param pools objects
     * @return List PoolDto
     */
    List<PoolDto> toPoolsDto(List<Pool> pools);

    /**
     * Mapping List pools to List PoolDto.
     *
     * @param poolDto and  existingPool objects
     */
    void updatePoolFromDto(PoolDto poolDto, @MappingTarget Pool existingPool);

}
