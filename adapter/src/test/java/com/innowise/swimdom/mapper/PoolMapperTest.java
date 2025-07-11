package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.openapi.model.PoolDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PoolMapperTest {

    private PoolMapper poolMapper;

    @BeforeEach
    void setUp() {
        PoolWorkingHoursMapper poolWorkingHoursMapper = new PoolWorkingHoursMapperImpl();
        poolMapper = new PoolMapperImpl(poolWorkingHoursMapper);
    }

    @Test
    void testToPool() {
        PoolDto dto = new PoolDto();
        dto.setName("Test Pool");
        dto.setCapacity(10);
        dto.setDescription("desc");
        dto.setLocation("loc");
        // poolWorkingHours can be tested separately

        Pool pool = poolMapper.toPool(dto);
        assertNull(pool.getId()); // id is ignored
        assertEquals(dto.getName(), pool.getName());
        assertEquals(dto.getCapacity(), pool.getCapacity());
        assertEquals(dto.getDescription(), pool.getDescription());
        assertEquals(dto.getLocation(), pool.getLocation());
    }

    @Test
    void testToPoolDto() {
        Pool pool = new Pool();
        pool.setId(UUID.randomUUID());
        pool.setName("Test Pool");
        pool.setCapacity(10);
        pool.setDescription("desc");
        pool.setLocation("loc");
        // poolWorkingHours can be tested separately

        PoolDto dto = poolMapper.toPoolDto(pool);
        assertEquals(pool.getId(), dto.getId());
        assertEquals(pool.getName(), dto.getName());
        assertEquals(pool.getCapacity(), dto.getCapacity());
        assertEquals(pool.getDescription(), dto.getDescription());
        assertEquals(pool.getLocation(), dto.getLocation());
    }

    @Test
    void testToPools() {
        PoolDto dto = new PoolDto();
        dto.setName("Test Pool");
        dto.setCapacity(10);
        List<Pool> pools = poolMapper.toPools(Collections.singletonList(dto));
        assertEquals(1, pools.size());
        assertEquals(dto.getName(), pools.get(0).getName());
    }

    @Test
    void testToPoolsDto() {
        Pool pool = new Pool();
        pool.setName("Test Pool");
        pool.setCapacity(10);
        List<PoolDto> dtos = poolMapper.toPoolsDto(Collections.singletonList(pool));
        assertEquals(1, dtos.size());
        assertEquals(pool.getName(), dtos.get(0).getName());
    }

    @Test
    void testUpdatePoolFromDto() {
        PoolDto dto = new PoolDto();
        dto.setName("Updated Name");
        dto.setCapacity(20);
        Pool pool = new Pool();
        pool.setName("Old Name");
        pool.setCapacity(10);
        poolMapper.updatePoolFromDto(dto, pool);
        assertEquals(dto.getName(), pool.getName());
        assertEquals(dto.getCapacity(), pool.getCapacity());
    }

    @Test
    void testToPool_NullInput() {
        assertNull(poolMapper.toPool(null));
    }

    @Test
    void testToPoolDto_NullInput() {
        assertNull(poolMapper.toPoolDto(null));
    }

    @Test
    void testToPools_NullInput() {
        assertNull(poolMapper.toPools(null));
    }

    @Test
    void testToPoolsDto_NullInput() {
        assertNull(poolMapper.toPoolsDto(null));
    }

    @Test
    void testUpdatePoolFromDto_NullInput() {
        Pool pool = new Pool();
        poolMapper.updatePoolFromDto(null, pool);
        assertNull(pool.getId());
    }

} 