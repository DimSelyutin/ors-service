package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.exceptions.PoolNotFoundException;
import com.innowise.swimdom.mapper.PoolMapper;
import com.innowise.swimdom.mapper.PoolWorkingHoursMapper;
import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.openapi.model.PoolWorkingHoursDto;
import com.innowise.swimdom.repository.PoolRepository;
import com.innowise.swimdom.repository.PoolWorkingHoursRepository;
import com.innowise.swimdom.service.util.TestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.innowise.swimdom.service.util.TestData.createPoolDto;
import static com.innowise.swimdom.service.util.TestData.testPoolDto;
import static com.innowise.swimdom.service.util.TestData.testPoolEntity;
import static com.innowise.swimdom.service.util.TestData.testPoolWorkingHours;
import static com.innowise.swimdom.service.util.TestData.testPoolWorkingHoursDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(value = MockitoExtension.class)
public class  PoolServiceImplTest {

    @Mock
    private PoolRepository poolRepository;

    @Mock
    private PoolMapper poolMapper;

    @Mock
    private PoolWorkingHoursRepository poolWorkingHoursRepository;

    @Mock
    private PoolWorkingHoursMapper poolWorkingHoursMapper;

    @InjectMocks
    private PoolServiceImpl poolService;

    private final PoolDto poolDto = mock(PoolDto.class);

    @Test
    void createPool() {
        // GIVEN
        PoolDto testPoolDto = TestData.testPoolDto;
        Pool poolEntity = testPoolEntity;

        when(poolMapper.toPool(testPoolDto)).thenReturn(poolEntity);
        when(poolRepository.save(poolEntity)).thenReturn(poolEntity);
        when(poolMapper.toPoolDto(poolEntity)).thenReturn(testPoolDto);
        // WHEN
        PoolDto result = poolService.createPool(testPoolDto);
        // THEN
        assertNotNull(result);
        assertEquals(testPoolDto, result);

        verify(poolMapper, times(1)).toPool(testPoolDto);
        verify(poolRepository, times(1)).save(poolEntity);
        verify(poolMapper, times(1)).toPoolDto(poolEntity);
    }

    @Test
    void updatePool_success() {
        // GIVEN
        PoolDto inputDto = testPoolDto;
        Pool existingPool = testPoolEntity;
        Pool updatedPool = testPoolEntity;
        PoolDto updatedDto = testPoolDto;

        when(poolRepository.findPoolById(inputDto.getId()))
            .thenReturn(Optional.of(existingPool));

        doAnswer(invocation -> {
            PoolDto dtoArg = invocation.getArgument(0);
            Pool poolArg = invocation.getArgument(1);

            return null;
        }).when(poolMapper).updatePoolFromDto(inputDto, existingPool);

        // WHEN
        when(poolRepository.save(existingPool)).thenReturn(updatedPool);
        when(poolMapper.toPoolDto(updatedPool)).thenReturn(updatedDto);
        PoolDto result = poolService.updatePool(inputDto);


        // THEN
        assertNotNull(result);
        assertEquals(updatedDto, result);

        verify(poolRepository).findPoolById(inputDto.getId());
        verify(poolMapper).updatePoolFromDto(inputDto, existingPool);
        verify(poolRepository).save(existingPool);
        verify(poolMapper).toPoolDto(updatedPool);
    }

    @Test
    void updatePool_nullId_throwsException() {
        PoolDto dtoWithoutId = new PoolDto();
        PoolNotFoundException exception = assertThrows(PoolNotFoundException.class,
            () -> poolService.updatePool(dtoWithoutId));
        assertEquals("Pool not found with id: null", exception.getMessage());
    }

    @Test
    void updatePool_poolNotFound_throwsPoolNotFoundException() {
        // GIVEN
        PoolDto inputDto = testPoolDto;
        // WHEN
        when(poolRepository.findPoolById(inputDto.getId())).thenReturn(Optional.empty());

        PoolNotFoundException exception = assertThrows(PoolNotFoundException.class,
            () -> poolService.updatePool(inputDto));
        // THEN
        assertEquals("Pool not found with id: " + inputDto.getId(), exception.getMessage());

        verify(poolRepository).findPoolById(inputDto.getId());
        verifyNoMoreInteractions(poolMapper, poolRepository);
    }

    @Test
    void deletePool_shouldCallExistsAndDeleteById() {
        // GIVEN
        UUID poolId = TestData.POOL_ID;
        PoolDto poolDto = mock(PoolDto.class);
        // WHEN
        when(poolDto.getId()).thenReturn(poolId);
        when(poolRepository.existsById(poolId)).thenReturn(true);

        poolService.deletePoolById(poolDto.getId());

        // THEN
        verify(poolDto).getId();
        verify(poolRepository).existsById(poolId);
        verify(poolRepository).deleteById(poolId);
    }

    @Test
    void deletePool_whenPoolDoesNotExist_shouldStillCallDeleteById() {
        // GIVEN
        UUID poolId = TestData.POOL_ID;
        PoolDto poolDto = mock(PoolDto.class);
        // WHEN
        when(poolDto.getId()).thenReturn(poolId);
        when(poolRepository.existsById(poolId)).thenReturn(false);

        poolService.deletePoolById(poolDto.getId());

        // THEN
        verify(poolDto).getId();
        verify(poolRepository).existsById(poolId);
        verify(poolRepository).deleteById(poolId);
    }

    @Test
    void createOrUpdateWorkingHours_poolNotFound_throwsException() {
        // GIVEN
        UUID poolId = TestData.POOL_ID;

        // WHEN
        when(poolDto.getId()).thenReturn(poolId);

        PoolWorkingHoursDto dto = mock(PoolWorkingHoursDto.class);

        Set<PoolWorkingHoursDto> dtoList = Set.of(dto);

        when(poolRepository.findPoolById(poolId)).thenReturn(Optional.empty());

        PoolNotFoundException ex = assertThrows(PoolNotFoundException.class,
            () -> poolService.createOrUpdateWorkingHours(createPoolDto()));

        assertEquals("Pool not found with id " + poolId, ex.getMessage());
        // THEN
        verify(poolRepository).findPoolById(poolId);
        verifyNoMoreInteractions(poolWorkingHoursRepository, poolWorkingHoursMapper);
    }

    @Test
    void createOrUpdateWorkingHours_validInput_deletesOldAndSavesNew() {
        // GIVEN
        Set<PoolWorkingHours> newSet = Set.of(testPoolWorkingHours);
        Set<PoolWorkingHoursDto> newSetDto = Set.of(testPoolWorkingHoursDto);

        when(poolRepository.findPoolById(testPoolEntity.getId())).thenReturn(Optional.of(testPoolEntity));
        when(poolWorkingHoursMapper.toPoolWorkingHoursList(eq(newSetDto))).thenReturn(newSet);
        when(poolWorkingHoursRepository.saveAll(eq(newSet))).thenReturn(newSet);

        // WHEN
        Set<PoolWorkingHours> result = poolService.createOrUpdateWorkingHours(testPoolDto);

        // THEN
        assertEquals(newSet, result);

        verify(poolRepository).findPoolById(testPoolDto.getId());
        verify(poolWorkingHoursRepository).deleteAllByPoolId(testPoolDto.getId());
        verify(poolWorkingHoursMapper).toPoolWorkingHoursList(eq(newSetDto));
        verify(poolWorkingHoursRepository).saveAll(eq(newSet));
    }



    @Test
    void getPoolsByDayOfWeek_whenPoolsFound_returnsDtoList() {
        // GIVEN
        Short dayOfWeek = 2;
        List<Pool> pools = List.of(testPoolEntity, testPoolEntity);
        List<PoolDto> poolDtos = List.of(testPoolDto, testPoolDto);
        // WHEN
        when(poolRepository.findPoolsByDayOfWeek(dayOfWeek)).thenReturn(Optional.of(pools));
        when(poolMapper.toPoolsDto(pools)).thenReturn(poolDtos);

        List<PoolDto> result = poolService.getPoolsByDayOfWeek(dayOfWeek);
        // THEN
        assertEquals(poolDtos, result);
        verify(poolRepository).findPoolsByDayOfWeek(dayOfWeek);
        verify(poolMapper).toPoolsDto(pools);
    }

    @Test
    void getPoolsByDayOfWeek_whenNoPoolsFound_throwsPoolNotFoundException() {
        // GIVEN
        Short dayOfWeek = 5;
        // WHEN
        when(poolRepository.findPoolsByDayOfWeek(dayOfWeek)).thenReturn(Optional.empty());

        PoolNotFoundException exception = assertThrows(PoolNotFoundException.class,
            () -> poolService.getPoolsByDayOfWeek(dayOfWeek));
        // THEN
        assertTrue(exception.getMessage().contains(dayOfWeek.toString()));
        verify(poolRepository).findPoolsByDayOfWeek(dayOfWeek);
        verifyNoInteractions(poolMapper);
    }

    @Test
    void searchPools_shouldReturnMappedPoolDtos() {
        // GIVEN
        PoolDto filterDto = new PoolDto();
        filterDto.setName("SwimDom");

        List<Pool> poolEntities = List.of(testPoolEntity);
        List<PoolDto> poolDtos = List.of(testPoolDto);

        when(poolRepository.findAll(any(Specification.class))).thenReturn(poolEntities);
        when(poolMapper.toPoolsDto(poolEntities)).thenReturn(poolDtos);
        assertEquals(poolDtos.get(0).getName(), poolEntities.get(0).getName());

        // WHEN
        List<PoolDto> result = poolService.searchPools(filterDto);

        // THEN
        assertNotNull(result);
        assertEquals(poolDtos, result);

        verify(poolRepository).findAll(any(Specification.class));
        verify(poolMapper).toPoolsDto(poolEntities);
    }

    @Test
    void createOrUpdateWorkingHours_shouldDeleteOldAndSaveNewHours() {
        // GIVEN
        UUID poolId = TestData.POOL_ID;

        PoolWorkingHoursDto dto1 = TestData.testPoolWorkingHoursDto;

        Set<PoolWorkingHoursDto> dtoList = Set.of(dto1);
        // WHEN
        when(poolRepository.findPoolById(poolId)).thenReturn(Optional.of(testPoolEntity));

        PoolWorkingHours pwh1 = new PoolWorkingHours();

        Set<PoolWorkingHours> mappedList = Set.of(pwh1);

        when(poolWorkingHoursMapper.toPoolWorkingHoursList(dtoList)).thenReturn(mappedList);

        when(poolWorkingHoursRepository.saveAll(mappedList)).thenAnswer(
            invocation -> invocation.getArgument(0));

        Set<PoolWorkingHours> result = poolService.createOrUpdateWorkingHours(createPoolDto());
        // THEN
        assertEquals(1, result.size());

        verify(poolRepository).findPoolById(poolId);
        verify(poolWorkingHoursRepository).deleteAllByPoolId(poolId);
        verify(poolWorkingHoursMapper).toPoolWorkingHoursList(dtoList);
        verify(poolWorkingHoursRepository).saveAll(mappedList);
    }

    @Test
    void createOrUpdateWorkingHours_shouldThrowException_whenDuplicateWeekdaysInDtoList() {
        // GIVEN
        Set<PoolWorkingHoursDto> dtoList =
            Set.of(TestData.testPoolWorkingHoursDto);
        // THEN
        assertThrows(PoolNotFoundException.class,
            () -> poolService.createOrUpdateWorkingHours(createPoolDto()));
        verifyNoMoreInteractions(poolWorkingHoursRepository, poolWorkingHoursMapper);
    }
}
