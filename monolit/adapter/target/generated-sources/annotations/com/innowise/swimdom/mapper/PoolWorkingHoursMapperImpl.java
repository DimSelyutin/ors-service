package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T18:03:58+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class PoolWorkingHoursMapperImpl implements PoolWorkingHoursMapper {

    @Override
    public PoolWorkingHours toPoolWorkingHours(PoolWorkingHoursDto dto) {
        if ( dto == null ) {
            return null;
        }

        PoolWorkingHours poolWorkingHours = new PoolWorkingHours();

        if ( dto.getWeekday() != null ) {
            poolWorkingHours.setWeekday( dto.getWeekday().shortValue() );
        }
        if ( dto.getOpenTime() != null ) {
            poolWorkingHours.setOpenTime( LocalTime.parse( dto.getOpenTime() ) );
        }
        if ( dto.getCloseTime() != null ) {
            poolWorkingHours.setCloseTime( LocalTime.parse( dto.getCloseTime() ) );
        }

        poolWorkingHours.setUpdatedAt( java.time.LocalDateTime.now() );

        return poolWorkingHours;
    }

    @Override
    public PoolWorkingHoursDto toPoolWorkingHoursDto(PoolWorkingHours entity) {
        if ( entity == null ) {
            return null;
        }

        PoolWorkingHoursDto poolWorkingHoursDto = new PoolWorkingHoursDto();

        poolWorkingHoursDto.setPoolId( entityPoolId( entity ) );
        if ( entity.getWeekday() != null ) {
            poolWorkingHoursDto.setWeekday( entity.getWeekday().intValue() );
        }
        if ( entity.getOpenTime() != null ) {
            poolWorkingHoursDto.setOpenTime( DateTimeFormatter.ISO_LOCAL_TIME.format( entity.getOpenTime() ) );
        }
        if ( entity.getCloseTime() != null ) {
            poolWorkingHoursDto.setCloseTime( DateTimeFormatter.ISO_LOCAL_TIME.format( entity.getCloseTime() ) );
        }

        return poolWorkingHoursDto;
    }

    @Override
    public List<PoolWorkingHours> toPoolWorkingHoursList(Set<PoolWorkingHoursDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<PoolWorkingHours> list = new ArrayList<PoolWorkingHours>( dto.size() );
        for ( PoolWorkingHoursDto poolWorkingHoursDto : dto ) {
            list.add( toPoolWorkingHours( poolWorkingHoursDto ) );
        }

        return list;
    }

    @Override
    public Set<PoolWorkingHoursDto> toPoolWorkingHoursDtoList(Set<PoolWorkingHours> entities) {
        if ( entities == null ) {
            return null;
        }

        Set<PoolWorkingHoursDto> set = new LinkedHashSet<PoolWorkingHoursDto>( Math.max( (int) ( entities.size() / .75f ) + 1, 16 ) );
        for ( PoolWorkingHours poolWorkingHours : entities ) {
            set.add( toPoolWorkingHoursDto( poolWorkingHours ) );
        }

        return set;
    }

    @Override
    public void updatePoolWorkingHoursFromDto(PoolWorkingHoursDto dto, PoolWorkingHours entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getPool() == null ) {
            entity.setPool( new Pool() );
        }
        poolWorkingHoursDtoToPool( dto, entity.getPool() );
        if ( dto.getWeekday() != null ) {
            entity.setWeekday( dto.getWeekday().shortValue() );
        }
        else {
            entity.setWeekday( null );
        }
        if ( dto.getOpenTime() != null ) {
            entity.setOpenTime( LocalTime.parse( dto.getOpenTime() ) );
        }
        else {
            entity.setOpenTime( null );
        }
        if ( dto.getCloseTime() != null ) {
            entity.setCloseTime( LocalTime.parse( dto.getCloseTime() ) );
        }
        else {
            entity.setCloseTime( null );
        }

        entity.setUpdatedAt( java.time.LocalDateTime.now() );
    }

    private UUID entityPoolId(PoolWorkingHours poolWorkingHours) {
        Pool pool = poolWorkingHours.getPool();
        if ( pool == null ) {
            return null;
        }
        return pool.getId();
    }

    protected void poolWorkingHoursDtoToPool(PoolWorkingHoursDto poolWorkingHoursDto, Pool mappingTarget) {
        if ( poolWorkingHoursDto == null ) {
            return;
        }

        mappingTarget.setId( poolWorkingHoursDto.getPoolId() );
    }
}
