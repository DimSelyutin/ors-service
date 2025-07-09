package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.exceptions.UserSubscriptionNotFoundException;
import com.innowise.swimdom.mapper.UserSubscriptionMapper;
import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;
import com.innowise.swimdom.repository.UserSubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSubscriptionServiceImplTest {

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private UserSubscriptionMapper userSubscriptionMapper;

    @InjectMocks
    private UserSubscriptionServiceImpl userSubscriptionService;

    private UUID existingId;
    private UserSubscription userSubscriptionEntity;
    private UserSubscriptionDTO userSubscriptionDTO;
    private UserSubscriptionCreateDTO createDTO;
    private UserSubscriptionUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        existingId = UUID.randomUUID();

        userSubscriptionEntity = new UserSubscription();
        userSubscriptionEntity.setId(existingId);
        // userSubscriptionEntity.setSubscription("Test Des");
        // заполните остальные поля сущности, если есть

        userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setId(existingId);
        // userSubscriptionDTO.setName("Test UserSubscription");
        // заполните остальные поля DTO

        createDTO = new UserSubscriptionCreateDTO();
        // createDTO.setName("New UserSubscription");
        // заполните поля создания

        updateDTO = new UserSubscriptionUpdateDTO();
        // updateDTO.setName("Updated UserSubscription");
        // заполните поля обновления
    }

    @Test
    void getAllUserSubscriptions_returnsList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<UserSubscription> entities = List.of(userSubscriptionEntity);
        Page<UserSubscription> page = new PageImpl<>(entities, pageable, entities.size());

        List<UserSubscriptionDTO> dtoList = List.of(userSubscriptionDTO);

        when(userSubscriptionRepository.findAll(pageable)).thenReturn(page);
        when(userSubscriptionMapper.toUserSubscriptionDtoList(entities)).thenReturn(dtoList);

        List<UserSubscriptionDTO> result = userSubscriptionService.getAllUserSubscriptions(pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userSubscriptionDTO.getId(), result.get(0).getId());

        verify(userSubscriptionRepository).findAll(pageable);
        verify(userSubscriptionMapper).toUserSubscriptionDtoList(entities);
    }

    @Test
    void getUserSubscriptionById_existingId_returnsDTO() {
        when(userSubscriptionRepository.findById(existingId)).thenReturn(Optional.of(userSubscriptionEntity));
        when(userSubscriptionMapper.toUserSubscriptionDto(userSubscriptionEntity)).thenReturn(userSubscriptionDTO);

        UserSubscriptionDTO result = userSubscriptionService.getUserSubscriptionById(existingId);

        assertNotNull(result);
        assertEquals(userSubscriptionDTO.getId(), result.getId());

        verify(userSubscriptionRepository).findById(existingId);
        verify(userSubscriptionMapper).toUserSubscriptionDto(userSubscriptionEntity);
    }

    @Test
    void getUserSubscriptionById_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(userSubscriptionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(UserSubscriptionNotFoundException.class,
            () -> userSubscriptionService.getUserSubscriptionById(nonExistingId));

        verify(userSubscriptionRepository).findById(nonExistingId);
        verifyNoInteractions(userSubscriptionMapper);
    }

    @Test
    void createUserSubscription_success() {
        UserSubscription newEntity = new UserSubscription();
        UserSubscription savedEntity = new UserSubscription();
        savedEntity.setId(UUID.randomUUID());


        UserSubscriptionDTO savedDTO = new UserSubscriptionDTO();
        savedDTO.setId(savedEntity.getId());

        when(userSubscriptionMapper.toUserSubscription(createDTO)).thenReturn(newEntity);
        when(userSubscriptionRepository.save(newEntity)).thenReturn(savedEntity);
        when(userSubscriptionMapper.toUserSubscriptionDto(savedEntity)).thenReturn(savedDTO);

        UserSubscriptionDTO result = userSubscriptionService.createUserSubscription(createDTO);

        assertNotNull(result);
        assertEquals(savedDTO.getId(), result.getId());


        verify(userSubscriptionMapper).toUserSubscription(createDTO);
        verify(userSubscriptionRepository).save(newEntity);
        verify(userSubscriptionMapper).toUserSubscriptionDto(savedEntity);
    }

    @Test
    void updateUserSubscription_existingId_success() {
        when(userSubscriptionRepository.findById(existingId)).thenReturn(Optional.of(userSubscriptionEntity));
        doNothing().when(userSubscriptionMapper).updateUserSubscriptionFromDto(updateDTO, userSubscriptionEntity);
        when(userSubscriptionRepository.save(userSubscriptionEntity)).thenReturn(userSubscriptionEntity);
        when(userSubscriptionMapper.toUserSubscriptionDto(userSubscriptionEntity)).thenReturn(userSubscriptionDTO);

        UserSubscriptionDTO result = userSubscriptionService.updateUserSubscription(existingId, updateDTO);

        assertNotNull(result);
        assertEquals(userSubscriptionDTO.getId(), result.getId());

        verify(userSubscriptionRepository).findById(existingId);
        verify(userSubscriptionMapper).updateUserSubscriptionFromDto(updateDTO, userSubscriptionEntity);
        verify(userSubscriptionRepository).save(userSubscriptionEntity);
        verify(userSubscriptionMapper).toUserSubscriptionDto(userSubscriptionEntity);
    }

    @Test
    void updateUserSubscription_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(userSubscriptionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(UserSubscriptionNotFoundException.class,
            () -> userSubscriptionService.updateUserSubscription(nonExistingId, updateDTO));

        verify(userSubscriptionRepository).findById(nonExistingId);
        verifyNoMoreInteractions(userSubscriptionMapper);
    }

    @Test
    void deleteUserSubscription_existingId_success() {
        when(userSubscriptionRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(userSubscriptionRepository).deleteById(existingId);

        assertDoesNotThrow(() -> userSubscriptionService.deleteUserSubscription(existingId));

        verify(userSubscriptionRepository).existsById(existingId);
        verify(userSubscriptionRepository).deleteById(existingId);
    }

    @Test
    void deleteUserSubscription_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(userSubscriptionRepository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(UserSubscriptionNotFoundException.class,
            () -> userSubscriptionService.deleteUserSubscription(nonExistingId));

        verify(userSubscriptionRepository).existsById(nonExistingId);
        verify(userSubscriptionRepository, never()).deleteById(any());
    }
}
