package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Subscription;
<<<<<<< Updated upstream
import com.innowise.swimdom.exception.SubscriptionNotFoundException;
import com.innowise.swimdom.mapper.SubscriptionMapper;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionFilterDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import com.innowise.swimdom.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
=======
import com.innowise.swimdom.exceptions.SubscriptionNotFoundException;
import com.innowise.swimdom.mapper.SubscriptionMapper;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import com.innowise.swimdom.repository.SubscriptionRepository;
import org.junit.Test;
>>>>>>> Stashed changes
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
<<<<<<< Updated upstream
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
=======

>>>>>>> Stashed changes
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
<<<<<<< Updated upstream
import static org.mockito.Mockito.times;
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
        // заполните другие поля сущности, если есть
>>>>>>> Stashed changes

        subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setId(existingId);
        subscriptionDTO.setName("Test Subscription");
<<<<<<< Updated upstream

        createDTO = new SubscriptionCreateDTO();
        createDTO.setName("New Subscription");

        updateDTO = new SubscriptionUpdateDTO();
        updateDTO.setId(existingId);
        updateDTO.setName("Updated Subscription");
    }

    @Test
    public void testGetAllSubscriptions() {
        // GIVEN
        SubscriptionFilterDTO filterDTO = new SubscriptionFilterDTO();
        filterDTO.setPrice(3000d);

        Subscription subscription1 = new Subscription();
        Subscription subscription2 = new Subscription();
        List<Subscription> subscriptionList = Arrays.asList(subscription1, subscription2);

        SubscriptionDTO dto1 = new SubscriptionDTO();
        SubscriptionDTO dto2 = new SubscriptionDTO();
        List<SubscriptionDTO> dtoList = Arrays.asList(dto1, dto2);

        when(subscriptionRepository.findAll(any(Specification.class))).thenReturn(subscriptionList);

        when(subscriptionMapper.toSubscriptionDTOList(subscriptionList)).thenReturn(dtoList);

        // WHEN
        List<SubscriptionDTO> result = subscriptionService.getAllSubscriptions(filterDTO);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dtoList, result);

        verify(subscriptionRepository, times(1)).findAll(any(Specification.class));
        verify(subscriptionMapper, times(1)).toSubscriptionDTOList(subscriptionList);
=======
        // заполните другие поля DTO

        createDTO = new SubscriptionCreateDTO();
        createDTO.setName("New Subscription");
        // заполните поля для создания

        updateDTO = new SubscriptionUpdateDTO();
        updateDTO.setName("Updated Subscription");
        // заполните поля для обновления
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        verify(subscriptionRepository, times(1)).findById(nonExistingId);
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream


=======
>>>>>>> Stashed changes
        when(subscriptionMapper.toSubscriptionDTO(savedEntity)).thenReturn(savedDTO);

        SubscriptionDTO result = subscriptionService.createSubscription(createDTO);

        assertNotNull(result);
        assertEquals(savedDTO.getId(), result.getId());
        assertEquals(savedDTO.getName(), result.getName());

        verify(subscriptionMapper).toSubscriptionEntity(createDTO);
<<<<<<< Updated upstream
        verify(subscriptionRepository, times(1)).save(newEntity);
=======
        verify(subscriptionRepository).save(newEntity);
>>>>>>> Stashed changes
        verify(subscriptionMapper).toSubscriptionDTO(savedEntity);
    }

    @Test
    public void updateSubscription_existingId_updatesAndReturnsDTO() {
        when(subscriptionRepository.findById(existingId)).thenReturn(Optional.of(subscriptionEntity));
        doNothing().when(subscriptionMapper).updateSubscriptionFromDTO(updateDTO, subscriptionEntity);
        when(subscriptionRepository.save(subscriptionEntity)).thenReturn(subscriptionEntity);
        when(subscriptionMapper.toSubscriptionDTO(subscriptionEntity)).thenReturn(subscriptionDTO);
<<<<<<< Updated upstream
        SubscriptionDTO result = subscriptionService.updateSubscription(updateDTO);
=======

        SubscriptionDTO result = subscriptionService.updateSubscription(existingId, updateDTO);
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
        SubscriptionUpdateDTO subscriptionUpdateDTO = new SubscriptionUpdateDTO();
        subscriptionUpdateDTO.setId(nonExistingId);
        subscriptionUpdateDTO.setName("Pool");
        assertThrows(SubscriptionNotFoundException.class,
            () -> subscriptionService.updateSubscription(subscriptionUpdateDTO));
=======

        assertThrows(SubscriptionNotFoundException.class,
            () -> subscriptionService.updateSubscription(nonExistingId, updateDTO));
>>>>>>> Stashed changes

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
