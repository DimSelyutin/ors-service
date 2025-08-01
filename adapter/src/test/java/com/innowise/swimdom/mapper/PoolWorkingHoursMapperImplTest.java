package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoolWorkingHoursMapperImplTest {

    private PoolWorkingHoursMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new PoolWorkingHoursMapperImpl();
    }

    @Test
    void testToPoolWorkingHours_NullInput() {
        assertNull(mapper.toPoolWorkingHours(null));
    }

    @Test
    void testToPoolWorkingHours_AllFieldsSet() {
        PoolWorkingHoursDto dto = new PoolWorkingHoursDto();
        dto.setWeekday(3);
        dto.setOpenTime("08:30:00");
        dto.setCloseTime("18:45:00");

        PoolWorkingHours entity = mapper.toPoolWorkingHours(dto);

        assertNotNull(entity);
        assertEquals((short)3, entity.getWeekday());
        assertEquals(LocalTime.of(8,30,0), entity.getOpenTime());
        assertEquals(LocalTime.of(18,45,0), entity.getCloseTime());
    }

    @Test
    void testToPoolWorkingHours_PartialFields() {
        PoolWorkingHoursDto dto = new PoolWorkingHoursDto();
        dto.setWeekday(null);
        dto.setOpenTime(null);
        dto.setCloseTime("20:00:00");

        PoolWorkingHours entity = mapper.toPoolWorkingHours(dto);

        assertNotNull(entity);
        assertNull(entity.getWeekday());
        assertNull(entity.getOpenTime());
        assertEquals(LocalTime.of(20,0), entity.getCloseTime());
    }

    @Test
    void testToPoolWorkingHoursDto_NullInput() {
        assertNull(mapper.toPoolWorkingHoursDto(null));
    }

    @Test
    void testToPoolWorkingHoursDto_AllFieldsSet() {
        PoolWorkingHours entity = new PoolWorkingHours();
        entity.setWeekday((short)5);
        entity.setOpenTime(LocalTime.of(9,15));
        entity.setCloseTime(LocalTime.of(19,0));
        Pool pool = new Pool();
        UUID poolId = UUID.randomUUID();
        pool.setId(poolId);
        entity.setPool(pool);

        PoolWorkingHoursDto dto = mapper.toPoolWorkingHoursDto(entity);

        assertNotNull(dto);
        assertEquals(poolId, dto.getPoolId());
        assertEquals(5, dto.getWeekday());
        assertEquals("09:15:00", dto.getOpenTime());
        assertEquals("19:00:00", dto.getCloseTime());
    }

    @Test
    void testToPoolWorkingHoursDto_PartialFields() {
        PoolWorkingHours entity = new PoolWorkingHours();
        entity.setWeekday(null);
        entity.setOpenTime(null);
        entity.setCloseTime(null);
        entity.setPool(null);

        PoolWorkingHoursDto dto = mapper.toPoolWorkingHoursDto(entity);

        assertNotNull(dto);
        assertNull(dto.getPoolId());
        assertNull(dto.getWeekday());
        assertNull(dto.getOpenTime());
        assertNull(dto.getCloseTime());
    }

    @Test
    void testToPoolWorkingHoursList_NullInput() {
        assertNull(mapper.toPoolWorkingHoursList(null));
    }

    @Test
    void testToPoolWorkingHoursList_EmptySet() {
        Set<PoolWorkingHoursDto> input = Collections.emptySet();
        List<PoolWorkingHours> list = mapper.toPoolWorkingHoursList(input);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void testToPoolWorkingHoursList_NonEmpty() {
        PoolWorkingHoursDto dto1 = new PoolWorkingHoursDto();
        dto1.setWeekday(1);
        PoolWorkingHoursDto dto2 = new PoolWorkingHoursDto();
        dto2.setWeekday(2);

        Set<PoolWorkingHoursDto> input = new LinkedHashSet<>(Arrays.asList(dto1, dto2));
        List<PoolWorkingHours> list = mapper.toPoolWorkingHoursList(input);

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals((short)1, list.get(0).getWeekday());
        assertEquals((short)2, list.get(1).getWeekday());
    }

    @Test
    void testToPoolWorkingHoursDtoList_NullInput() {
        assertNull(mapper.toPoolWorkingHoursDtoList(null));
    }

    @Test
    void testToPoolWorkingHoursDtoList_EmptySet() {
        Set<PoolWorkingHours> input = Collections.emptySet();
        Set<PoolWorkingHoursDto> result = mapper.toPoolWorkingHoursDtoList(input);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToPoolWorkingHoursDtoList_NonEmpty() {
        PoolWorkingHours ph1 = new PoolWorkingHours();
        ph1.setWeekday((short)1);
        PoolWorkingHours ph2 = new PoolWorkingHours();
        ph2.setWeekday((short)2);

        Set<PoolWorkingHours> input = new LinkedHashSet<>(Arrays.asList(ph1, ph2));
        Set<PoolWorkingHoursDto> result = mapper.toPoolWorkingHoursDtoList(input);

        assertNotNull(result);
        assertEquals(2, result.size());
        List<Integer> weekdays = new ArrayList<>();
        for (PoolWorkingHoursDto dto : result) {
            weekdays.add(dto.getWeekday());
        }
        assertTrue(weekdays.contains(1));
        assertTrue(weekdays.contains(2));
    }

    @Test
    void testUpdatePoolWorkingHoursFromDto_NullDto() {
        PoolWorkingHours entity = new PoolWorkingHours();
        entity.setPool(null);
        entity.setWeekday((short)1);
        entity.setOpenTime(LocalTime.NOON);
        entity.setCloseTime(LocalTime.MIDNIGHT);

        mapper.updatePoolWorkingHoursFromDto(null, entity);

        // entity fields should remain unchanged
        assertNull(entity.getPool()); // because dto == null, pool не создается
        assertEquals((short)1, entity.getWeekday());
        assertEquals(LocalTime.NOON, entity.getOpenTime());
        assertEquals(LocalTime.MIDNIGHT, entity.getCloseTime());
    }

    @Test
    void testUpdatePoolWorkingHoursFromDto_PopulatesAllFields() {
        PoolWorkingHoursDto dto = new PoolWorkingHoursDto();
        UUID poolId = UUID.randomUUID();
        dto.setPoolId(poolId);
        dto.setWeekday(7);
        dto.setOpenTime("07:00:00");
        dto.setCloseTime("22:00:00");

        PoolWorkingHours entity = new PoolWorkingHours();
        entity.setPool(null);
        entity.setWeekday((short)1);
        entity.setOpenTime(LocalTime.NOON);
        entity.setCloseTime(LocalTime.MIDNIGHT);

        mapper.updatePoolWorkingHoursFromDto(dto, entity);

        assertNotNull(entity.getPool());
        assertEquals(poolId, entity.getPool().getId());
        assertEquals((short)7, entity.getWeekday());
        assertEquals(LocalTime.of(7,0), entity.getOpenTime());
        assertEquals(LocalTime.of(22,0), entity.getCloseTime());
    }

    @Test
    void testUpdatePoolWorkingHoursFromDto_NullsClearFields() {
        PoolWorkingHoursDto dto = new PoolWorkingHoursDto();
        dto.setPoolId(null);
        dto.setWeekday(null);
        dto.setOpenTime(null);
        dto.setCloseTime(null);

        PoolWorkingHours entity = new PoolWorkingHours();
        Pool pool = new Pool();
        pool.setId(UUID.randomUUID());
        entity.setPool(pool);
        entity.setWeekday((short)5);
        entity.setOpenTime(LocalTime.of(10,0));
        entity.setCloseTime(LocalTime.of(20,0));

        mapper.updatePoolWorkingHoursFromDto(dto, entity);

        assertNotNull(entity.getPool());
        assertNull(entity.getPool().getId());
        assertNull(entity.getWeekday());
        assertNull(entity.getOpenTime());
        assertNull(entity.getCloseTime());
    }
}
