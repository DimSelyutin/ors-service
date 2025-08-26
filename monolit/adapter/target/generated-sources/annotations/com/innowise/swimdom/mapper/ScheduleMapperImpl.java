package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.openapi.model.ScheduleDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T18:03:59+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class ScheduleMapperImpl implements ScheduleMapper {

    @Override
    public Schedule toSchedule(ScheduleDto scheduleDto) {
        if ( scheduleDto == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        schedule.setPool( scheduleDtoToPool( scheduleDto ) );
        schedule.setStartDatetime( scheduleDto.getStartDatetime() );
        schedule.setEndDatetime( scheduleDto.getEndDatetime() );

        schedule.setUpdatedAt( java.time.LocalDateTime.now() );

        return schedule;
    }

    @Override
    public ScheduleDto toScheduleDto(Schedule schedule) {
        if ( schedule == null ) {
            return null;
        }

        ScheduleDto scheduleDto = new ScheduleDto();

        scheduleDto.setPoolId( schedulePoolId( schedule ) );
        scheduleDto.setId( schedule.getId() );
        scheduleDto.setStartDatetime( schedule.getStartDatetime() );
        scheduleDto.setEndDatetime( schedule.getEndDatetime() );
        scheduleDto.setCreatedAt( schedule.getCreatedAt() );
        scheduleDto.setUpdatedAt( schedule.getUpdatedAt() );

        return scheduleDto;
    }

    @Override
    public List<Schedule> toSchedules(List<ScheduleDto> schedulesDto) {
        if ( schedulesDto == null ) {
            return null;
        }

        List<Schedule> list = new ArrayList<Schedule>( schedulesDto.size() );
        for ( ScheduleDto scheduleDto : schedulesDto ) {
            list.add( toSchedule( scheduleDto ) );
        }

        return list;
    }

    @Override
    public List<ScheduleDto> toSchedulesDto(List<Schedule> schedules) {
        if ( schedules == null ) {
            return null;
        }

        List<ScheduleDto> list = new ArrayList<ScheduleDto>( schedules.size() );
        for ( Schedule schedule : schedules ) {
            list.add( toScheduleDto( schedule ) );
        }

        return list;
    }

    @Override
    public void updateScheduleFromDto(ScheduleDto scheduleDto, Schedule existingSchedule) {
        if ( scheduleDto == null ) {
            return;
        }

        if ( existingSchedule.getPool() == null ) {
            existingSchedule.setPool( new Pool() );
        }
        scheduleDtoToPool1( scheduleDto, existingSchedule.getPool() );
        existingSchedule.setStartDatetime( scheduleDto.getStartDatetime() );
        existingSchedule.setEndDatetime( scheduleDto.getEndDatetime() );

        existingSchedule.setUpdatedAt( java.time.LocalDateTime.now() );
    }

    protected Pool scheduleDtoToPool(ScheduleDto scheduleDto) {
        if ( scheduleDto == null ) {
            return null;
        }

        Pool pool = new Pool();

        pool.setId( scheduleDto.getPoolId() );

        return pool;
    }

    private UUID schedulePoolId(Schedule schedule) {
        Pool pool = schedule.getPool();
        if ( pool == null ) {
            return null;
        }
        return pool.getId();
    }

    protected void scheduleDtoToPool1(ScheduleDto scheduleDto, Pool mappingTarget) {
        if ( scheduleDto == null ) {
            return;
        }

        mappingTarget.setId( scheduleDto.getPoolId() );
    }
}
