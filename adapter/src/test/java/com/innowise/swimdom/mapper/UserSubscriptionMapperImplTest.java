package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserSubscriptionMapperImplTest {

    private UserSubscriptionMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserSubscriptionMapperImpl();
    }

    @Test
    void toUserSubscriptionDto_NullInput_ReturnsNull() {
        assertNull(mapper.toUserSubscriptionDto(null));
    }

    @Test
    void toUserSubscriptionDto_AllFieldsSet() {
        UUID userId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);

        UserSubscription entity = new UserSubscription();
        entity.setUser(user);
        entity.setSubscription(subscription);
        entity.setId(id);
        entity.setEstimate(42);
        entity.setStartDate(LocalDate.of(2023, 1, 1));
        entity.setEndDate(LocalDate.of(2023, 12, 31));
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        UserSubscriptionDTO dto = mapper.toUserSubscriptionDto(entity);

        assertNotNull(dto);
        assertEquals(userId, dto.getUserId());
        assertEquals(subscriptionId, dto.getSubscriptionId());
        assertEquals(id, dto.getId());
        assertEquals(42, dto.getEstimate());
        assertEquals(LocalDate.of(2023, 1, 1), dto.getStartDate());
        assertEquals(LocalDate.of(2023, 12, 31), dto.getEndDate());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    void toUserSubscriptionDto_NullUserAndSubscription() {
        UserSubscription entity = new UserSubscription();
        entity.setUser(null);
        entity.setSubscription(null);
        entity.setId(null);
        entity.setEstimate(null);
        entity.setStartDate(null);
        entity.setEndDate(null);
        entity.setCreatedAt(null);
        entity.setUpdatedAt(null);

        UserSubscriptionDTO dto = mapper.toUserSubscriptionDto(entity);

        assertNotNull(dto);
        assertNull(dto.getUserId());
        assertNull(dto.getSubscriptionId());
        assertNull(dto.getId());
        assertNull(dto.getEstimate());
        assertNull(dto.getStartDate());
        assertNull(dto.getEndDate());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    void toUserSubscriptionDtoList_NullInput_ReturnsNull() {
        assertNull(mapper.toUserSubscriptionDtoList(null));
    }

    @Test
    void toUserSubscriptionDtoList_EmptyList_ReturnsEmptyList() {
        List<UserSubscriptionDTO> result = mapper.toUserSubscriptionDtoList(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toUserSubscriptionDtoList_NonEmptyList() {
        UserSubscription us1 = new UserSubscription();
        us1.setId(UUID.randomUUID());
        UserSubscription us2 = new UserSubscription();
        us2.setId(UUID.randomUUID());

        List<UserSubscription> list = Arrays.asList(us1, us2);
        List<UserSubscriptionDTO> dtoList = mapper.toUserSubscriptionDtoList(list);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(us1.getId(), dtoList.get(0).getId());
        assertEquals(us2.getId(), dtoList.get(1).getId());
    }

    @Test
    void toUserSubscriptionDTO_NullInput_ReturnsNull() {
        assertNull(mapper.toUserSubscription((UserSubscriptionDTO) null));
    }

    @Test
    void toUserSubscriptionDTO_AllFieldsSet() {
        UUID userId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        UserSubscriptionDTO dto = new UserSubscriptionDTO();
        dto.setUserId(userId);
        dto.setSubscriptionId(subscriptionId);
        dto.setId(id);
        dto.setEstimate(10);
        dto.setStartDate(LocalDate.of(2023, 5, 1));
        dto.setEndDate(LocalDate.of(2023, 10, 1));
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        UserSubscription entity = mapper.toUserSubscription(dto);

        assertNotNull(entity);
        assertNotNull(entity.getUser());
        assertEquals(userId, entity.getUser().getId());
        assertNotNull(entity.getSubscription());
        assertEquals(subscriptionId, entity.getSubscription().getId());
        assertEquals(10, entity.getEstimate());
        assertEquals(LocalDate.of(2023, 5, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2023, 10, 1), entity.getEndDate());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void toUserSubscriptionDTO_NullUserIdAndSubscriptionId() {
        UserSubscriptionDTO dto = new UserSubscriptionDTO();
        dto.setUserId(null);
        dto.setSubscriptionId(null);
        dto.setEstimate(null);
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setCreatedAt(null);
        dto.setUpdatedAt(null);

        UserSubscription entity = mapper.toUserSubscription(dto);

        assertNotNull(entity);
        assertNotNull(entity.getUser());
        assertNull(entity.getUser().getId());
        assertNotNull(entity.getSubscription());
        assertNull(entity.getSubscription().getId());
        assertNull(entity.getEstimate());
        assertNull(entity.getStartDate());
        assertNull(entity.getEndDate());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void toUserSubscription_FromCreateDTO_NullInput_ReturnsNull() {
        assertNull(mapper.toUserSubscription((UserSubscriptionCreateDTO) null));
    }

    @Test
    void toUserSubscription_FromCreateDTO_AllFieldsSet() {
        UUID userId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();

        UserSubscriptionCreateDTO createDTO = new UserSubscriptionCreateDTO();
        createDTO.setUserId(userId);
        createDTO.setSubscriptionId(subscriptionId);
        createDTO.setEstimate(5);
        createDTO.setStartDate(LocalDate.of(2023, 6, 1));
        createDTO.setEndDate(LocalDate.of(2023, 9, 1));

        UserSubscription entity = mapper.toUserSubscription(createDTO);

        assertNotNull(entity);
        assertNotNull(entity.getUser());
        assertEquals(userId, entity.getUser().getId());
        assertNotNull(entity.getSubscription());
        assertEquals(subscriptionId, entity.getSubscription().getId());
        assertEquals(5, entity.getEstimate());
        assertEquals(LocalDate.of(2023, 6, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2023, 9, 1), entity.getEndDate());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void updateUserSubscriptionFromDto_NullDto_DoesNotChangeEntity() {
        UserSubscription entity = new UserSubscription();
        entity.setEstimate(1);
        entity.setStartDate(LocalDate.now());
        entity.setEndDate(LocalDate.now().plusDays(1));
        entity.setUser(null);
        entity.setSubscription(null);

        mapper.updateUserSubscriptionFromDto(null, entity);

        assertEquals(1, entity.getEstimate());
        assertNotNull(entity.getStartDate());
        assertNotNull(entity.getEndDate());
        assertNull(entity.getUser());
        assertNull(entity.getSubscription());
    }

    @Test
    void updateUserSubscriptionFromDto_AllFieldsUpdated() {
        UserSubscriptionUpdateDTO updateDTO = new UserSubscriptionUpdateDTO();
        updateDTO.setEstimate(99);
        updateDTO.setStartDate(LocalDate.of(2024, 1, 1));
        updateDTO.setEndDate(LocalDate.of(2024, 12, 31));

        UserSubscription entity = new UserSubscription();
        entity.setEstimate(1);
        entity.setStartDate(LocalDate.of(2023, 1, 1));
        entity.setEndDate(LocalDate.of(2023, 12, 31));
        entity.setUser(null);
        entity.setSubscription(null);

        mapper.updateUserSubscriptionFromDto(updateDTO, entity);

        assertEquals(99, entity.getEstimate());
        assertEquals(LocalDate.of(2024, 1, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2024, 12, 31), entity.getEndDate());
        assertNotNull(entity.getUser());
        assertNotNull(entity.getSubscription());
    }
}
