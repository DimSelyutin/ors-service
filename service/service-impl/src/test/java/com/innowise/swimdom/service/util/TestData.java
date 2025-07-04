package com.innowise.swimdom.service.util;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TestData {

    public static final UUID POOL_ID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
    public static final String POOL_NAME = "SwimDom";
    public static final String POOL_DESCRIPTION = "Olympic size swimming pool";
    public static final String POOL_LOCATION = "123 Main St, Springfield";
    public static final int POOL_CAPACITY = 100;
    public static final LocalDateTime CREATED_AT = LocalDateTime.parse("2023-01-01T12:00:00");
    public static final LocalDateTime UPDATED_AT = LocalDateTime.parse("2023-01-02T12:00:00");

    public static final UUID POOL_WORKING_HOURS_ID = UUID.fromString("1c6e9f99-5b7e-4d3b-9b5d-7a2f8a9c1234");
    public static final int WEEKDAY_MONDAY = 1;
    public static final String OPEN_TIME = "08:00:00";
    public static final String CLOSE_TIME = "22:00:00";

    public static PoolWorkingHoursDto createPoolWorkingHoursDto() {
        PoolWorkingHoursDto workingHours = new PoolWorkingHoursDto();
        workingHours.setWeekday(TestData.WEEKDAY_MONDAY);
        workingHours.setOpenTime(TestData.OPEN_TIME);
        workingHours.setCloseTime(TestData.CLOSE_TIME);
        return workingHours;
    }

    public static PoolWorkingHours createPoolWorkingHours() {
        PoolWorkingHours workingHours = new PoolWorkingHours();
        workingHours.setWeekday((short) TestData.WEEKDAY_MONDAY);
        workingHours.setOpenTime(LocalTime.parse(TestData.OPEN_TIME));
        workingHours.setCloseTime(LocalTime.parse(TestData.CLOSE_TIME));
        return workingHours;
    }

    public static PoolDto createPoolDto() {
        PoolDto pool = new PoolDto();
        pool.setId(TestData.POOL_ID);
        pool.setName(TestData.POOL_NAME);
        pool.setDescription(TestData.POOL_DESCRIPTION);
        pool.setLocation(TestData.POOL_LOCATION);
        pool.setCapacity(TestData.POOL_CAPACITY);
        pool.setCreatedAt(TestData.CREATED_AT);
        pool.setUpdatedAt(TestData.UPDATED_AT);
        pool.setPoolWorkingHours(Set.of(createPoolWorkingHoursDto()));
        return pool;
    }

    public static Pool createPoolEntity() {
        Pool pool = new Pool();
        pool.setPoolWorkingHours(Set.of(createPoolWorkingHours()));
        pool.setId(POOL_ID);
        pool.setName(POOL_NAME);
        pool.setDescription(POOL_DESCRIPTION);
        pool.setLocation(POOL_LOCATION);
        pool.setCapacity(POOL_CAPACITY);
        pool.setCreatedAt(CREATED_AT);
        pool.setUpdatedAt(UPDATED_AT);
        return pool;
    }

    public static PoolDto testPoolDto = createPoolDto();
    public static PoolWorkingHoursDto testPoolWorkingHoursDto = createPoolWorkingHoursDto();
    public static final Pool testPoolEntity = createPoolEntity();
    public static final PoolWorkingHours testPoolWorkingHours = createPoolWorkingHours();
}