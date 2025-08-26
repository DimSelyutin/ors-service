package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T18:03:58+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class PoolMapperImpl implements PoolMapper {

    private final PoolWorkingHoursMapper poolWorkingHoursMapper;

    @Autowired
    public PoolMapperImpl(PoolWorkingHoursMapper poolWorkingHoursMapper) {

        this.poolWorkingHoursMapper = poolWorkingHoursMapper;
    }

    @Override
    public Pool toPool(PoolDto poolDto) {
        if ( poolDto == null ) {
            return null;
        }

        Pool pool = new Pool();

        pool.setPoolWorkingHours( poolWorkingHoursDtoSetToPoolWorkingHoursSet( poolDto.getPoolWorkingHours() ) );
        pool.setName( poolDto.getName() );
        pool.setDescription( poolDto.getDescription() );
        pool.setLocation( poolDto.getLocation() );
        pool.setCapacity( poolDto.getCapacity() );
        pool.setCreatedAt( poolDto.getCreatedAt() );
        pool.setUpdatedAt( poolDto.getUpdatedAt() );

        return pool;
    }

    @Override
    public PoolDto toPoolDto(Pool pool) {
        if ( pool == null ) {
            return null;
        }

        PoolDto poolDto = new PoolDto();

        poolDto.setId( pool.getId() );
        poolDto.setPoolWorkingHours( poolWorkingHoursMapper.toPoolWorkingHoursDtoList( pool.getPoolWorkingHours() ) );
        poolDto.setName( pool.getName() );
        poolDto.setDescription( pool.getDescription() );
        poolDto.setLocation( pool.getLocation() );
        poolDto.setCapacity( pool.getCapacity() );
        poolDto.setCreatedAt( pool.getCreatedAt() );
        poolDto.setUpdatedAt( pool.getUpdatedAt() );

        return poolDto;
    }

    @Override
    public List<Pool> toPools(List<PoolDto> poolsDto) {
        if ( poolsDto == null ) {
            return null;
        }

        List<Pool> list = new ArrayList<Pool>( poolsDto.size() );
        for ( PoolDto poolDto : poolsDto ) {
            list.add( toPool( poolDto ) );
        }

        return list;
    }

    @Override
    public List<PoolDto> toPoolsDto(List<Pool> pools) {
        if ( pools == null ) {
            return null;
        }

        List<PoolDto> list = new ArrayList<PoolDto>( pools.size() );
        for ( Pool pool : pools ) {
            list.add( toPoolDto( pool ) );
        }

        return list;
    }

    @Override
    public void updatePoolFromDto(PoolDto poolDto, Pool existingPool) {
        if ( poolDto == null ) {
            return;
        }

        existingPool.setId( poolDto.getId() );
        if ( existingPool.getPoolWorkingHours() != null ) {
            Set<PoolWorkingHours> set = poolWorkingHoursDtoSetToPoolWorkingHoursSet( poolDto.getPoolWorkingHours() );
            if ( set != null ) {
                existingPool.getPoolWorkingHours().clear();
                existingPool.getPoolWorkingHours().addAll( set );
            }
            else {
                existingPool.setPoolWorkingHours( null );
            }
        }
        else {
            Set<PoolWorkingHours> set = poolWorkingHoursDtoSetToPoolWorkingHoursSet( poolDto.getPoolWorkingHours() );
            if ( set != null ) {
                existingPool.setPoolWorkingHours( set );
            }
        }
        existingPool.setName( poolDto.getName() );
        existingPool.setDescription( poolDto.getDescription() );
        existingPool.setLocation( poolDto.getLocation() );
        existingPool.setCapacity( poolDto.getCapacity() );
        existingPool.setCreatedAt( poolDto.getCreatedAt() );
        existingPool.setUpdatedAt( poolDto.getUpdatedAt() );
    }

    protected Set<PoolWorkingHours> poolWorkingHoursDtoSetToPoolWorkingHoursSet(Set<PoolWorkingHoursDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<PoolWorkingHours> set1 = new LinkedHashSet<PoolWorkingHours>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( PoolWorkingHoursDto poolWorkingHoursDto : set ) {
            set1.add( poolWorkingHoursMapper.toPoolWorkingHours( poolWorkingHoursDto ) );
        }

        return set1;
    }
}
