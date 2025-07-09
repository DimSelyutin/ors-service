package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.exceptions.SubscriptionNotFoundException;
import com.innowise.swimdom.mapper.SubscriptionMapper;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import com.innowise.swimdom.repository.SubscriptionRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private UUID existingId;
    private Subscription subscriptionEntity;
    private SubscriptionDTO subscriptionDTO;
    private SubscriptionCreateDTO createDTO;
    private SubscriptionUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        existingId = UUID.randomUUID();

        subscriptionEntity = new Subscription();
        subscriptionEntity.setId(existingId);
        subscriptionEntity.setName("Test Subscription");
        // заполните другие поля сущности, если есть

        subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setId(existingId);
        subscriptionDTO.setName("Test Subscription");
        // заполните другие поля DTO

        createDTO = new SubscriptionCreateDTO();
        createDTO.setName("New Subscription");
        // заполните поля для создания

        updateDTO = new SubscriptionUpdateDTO();
        updateDTO.setName("Updated Subscription");
        // заполните поля для обновления
    }

    @Test
    public void getSubscriptionById_existingId_returnsDTO() {
        when(subscriptionRepository.findById(existingId)).thenReturn(Optional.of(subscriptionEntity));
        when(subscriptionMapper.toSubscriptionDTO(subscriptionEntity)).thenReturn(subscriptionDTO);

        SubscriptionDTO result = subscriptionService.getSubscriptionById(existingId);

        assertNotNull(result);
        assertEquals(subscriptionDTO.getId(), result.getId());
        verify(subscriptionRepository).findById(existingId);
        verify(subscriptionMapper).toSubscriptionDTO(subscriptionEntity);
    }

    @Test
    public void getSubscriptionById_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(subscriptionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class,
            () -> subscriptionService.getSubscriptionById(nonExistingId));

        verify(subscriptionRepository).findById(nonExistingId);
        verifyNoInteractions(subscriptionMapper);
    }

    @Test
    public void createSubscription_validCreateDTO_returnsCreatedDTO() {
        Subscription newEntity = new Subscription();
        newEntity.setName(createDTO.getName());

        Subscription savedEntity = new Subscription();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setName(createDTO.getName());

        SubscriptionDTO savedDTO = new SubscriptionDTO();
        savedDTO.setId(savedEntity.getId());
        savedDTO.setName(savedEntity.getName());

        when(subscriptionMapper.toSubscriptionEntity(createDTO)).thenReturn(newEntity);
        when(subscriptionRepository.save(newEntity)).thenReturn(savedEntity);
        when(subscriptionMapper.toSubscriptionDTO(savedEntity)).thenReturn(savedDTO);

        SubscriptionDTO result = subscriptionService.createSubscription(createDTO);

        assertNotNull(result);
        assertEquals(savedDTO.getId(), result.getId());
        assertEquals(savedDTO.getName(), result.getName());

        verify(subscriptionMapper).toSubscriptionEntity(createDTO);
        verify(subscriptionRepository).save(newEntity);
        verify(subscriptionMapper).toSubscriptionDTO(savedEntity);
    }

    @Test
    public void updateSubscription_existingId_updatesAndReturnsDTO() {
        when(subscriptionRepository.findById(existingId)).thenReturn(Optional.of(subscriptionEntity));
        doNothing().when(subscriptionMapper).updateSubscriptionFromDTO(updateDTO, subscriptionEntity);
        when(subscriptionRepository.save(subscriptionEntity)).thenReturn(subscriptionEntity);
        when(subscriptionMapper.toSubscriptionDTO(subscriptionEntity)).thenReturn(subscriptionDTO);

        SubscriptionDTO result = subscriptionService.updateSubscription(existingId, updateDTO);

        assertNotNull(result);
        assertEquals(subscriptionDTO.getId(), result.getId());

        verify(subscriptionRepository).findById(existingId);
        verify(subscriptionMapper).updateSubscriptionFromDTO(updateDTO, subscriptionEntity);
        verify(subscriptionRepository).save(subscriptionEntity);
        verify(subscriptionMapper).toSubscriptionDTO(subscriptionEntity);
    }

    @Test
    public void updateSubscription_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(subscriptionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class,
            () -> subscriptionService.updateSubscription(nonExistingId, updateDTO));

        verify(subscriptionRepository).findById(nonExistingId);
        verifyNoMoreInteractions(subscriptionMapper);
    }

    @Test
    public void deleteSubscription_existingId_deletesSuccessfully() {
        when(subscriptionRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(subscriptionRepository).deleteById(existingId);

        assertDoesNotThrow(() -> subscriptionService.deleteSubscription(existingId));

        verify(subscriptionRepository).existsById(existingId);
        verify(subscriptionRepository).deleteById(existingId);
    }

    @Test
    public void deleteSubscription_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(subscriptionRepository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(SubscriptionNotFoundException.class,
            () -> subscriptionService.deleteSubscription(nonExistingId));

        verify(subscriptionRepository).existsById(nonExistingId);
        verify(subscriptionRepository, never()).deleteById(any());
    }
}
