package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.enums.SubscriptionDuration;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionMapperImplTest {

    private SubscriptionMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new SubscriptionMapperImpl();
    }

    @Test
    void toSubscriptionEntity_NullInput_ReturnsNull() {
        assertNull(mapper.toSubscriptionEntity(null));
    }

    @Test
    void toSubscriptionEntity_AllFieldsSet() {
        SubscriptionCreateDTO createDTO = new SubscriptionCreateDTO();
        createDTO.setName("Test Name");
        createDTO.setDescription("Test Description");
        createDTO.setDuration(SubscriptionCreateDTO.DurationEnum.MONTH);
        createDTO.setPrice(19.99);

        Subscription entity = mapper.toSubscriptionEntity(createDTO);

        assertNotNull(entity);
        assertEquals("Test Name", entity.getName());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(SubscriptionDuration.MONTH, entity.getDuration());
        assertNotNull(entity.getPrice());
        assertEquals(BigDecimal.valueOf(19.99), entity.getPrice());
    }

    @Test
    void toSubscriptionEntity_NullPrice() {
        SubscriptionCreateDTO createDTO = new SubscriptionCreateDTO();
        createDTO.setName("Name");
        createDTO.setDescription("Desc");
        createDTO.setDuration(SubscriptionCreateDTO.DurationEnum.WEEK);
        createDTO.setPrice(null);

        Subscription entity = mapper.toSubscriptionEntity(createDTO);

        assertNotNull(entity);
        assertEquals("Name", entity.getName());
        assertEquals("Desc", entity.getDescription());
        assertEquals(SubscriptionDuration.WEEK, entity.getDuration());
        assertNull(entity.getPrice());
    }

    @Test
    void updateSubscriptionFromDTO_NullInput_DoesNothing() {
        Subscription subscription = new Subscription();
        subscription.setName("Old Name");
        subscription.setDescription("Old Desc");
        subscription.setDuration(SubscriptionDuration.YEAR);
        subscription.setPrice(BigDecimal.valueOf(100));

        mapper.updateSubscriptionFromDTO(null, subscription);

        assertEquals("Old Name", subscription.getName());
        assertEquals("Old Desc", subscription.getDescription());
        assertEquals(SubscriptionDuration.YEAR, subscription.getDuration());
        assertEquals(BigDecimal.valueOf(100), subscription.getPrice());
    }

    @Test
    void updateSubscriptionFromDTO_AllFieldsUpdated() {
        SubscriptionUpdateDTO updateDTO = new SubscriptionUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setDescription("New Desc");
        updateDTO.setDuration(SubscriptionUpdateDTO.DurationEnum.WEEK);
        updateDTO.setPrice(50.5);

        Subscription subscription = new Subscription();
        subscription.setName("Old Name");
        subscription.setDescription("Old Desc");
        subscription.setDuration(SubscriptionDuration.YEAR);
        subscription.setPrice(BigDecimal.valueOf(100));

        mapper.updateSubscriptionFromDTO(updateDTO, subscription);

        assertEquals("New Name", subscription.getName());
        assertEquals("New Desc", subscription.getDescription());
        assertEquals(SubscriptionDuration.WEEK, subscription.getDuration());
        assertEquals(BigDecimal.valueOf(50.5), subscription.getPrice());
    }

    @Test
    void updateSubscriptionFromDTO_NullPrice_SetsPriceNull() {
        SubscriptionUpdateDTO updateDTO = new SubscriptionUpdateDTO();
        updateDTO.setName("Name");
        updateDTO.setDescription("Desc");
        updateDTO.setDuration(SubscriptionUpdateDTO.DurationEnum.MONTH);
        updateDTO.setPrice(null);

        Subscription subscription = new Subscription();
        subscription.setPrice(BigDecimal.valueOf(123));

        mapper.updateSubscriptionFromDTO(updateDTO, subscription);

        assertNull(subscription.getPrice());
    }

    @Test
    void toSubscriptionDTO_NullInput_ReturnsNull() {
        assertNull(mapper.toSubscriptionDTO(null));
    }

    @Test
    void toSubscriptionDTO_AllFieldsSet() {
        Subscription subscription = new Subscription();
        UUID id = UUID.randomUUID();
        subscription.setId(id);
        subscription.setName("Name");
        subscription.setDescription("Description");
        subscription.setDuration(SubscriptionDuration.YEAR);
        subscription.setPrice(BigDecimal.valueOf(123.45));
        subscription.setCreatedAt(LocalDateTime.now());
        subscription.setUpdatedAt(LocalDateTime.now());

        SubscriptionDTO dto = mapper.toSubscriptionDTO(subscription);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Name", dto.getName());
        assertEquals("Description", dto.getDescription());
        assertEquals(SubscriptionDTO.DurationEnum.YEAR, dto.getDuration());
        assertEquals(123.45, dto.getPrice());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    void toSubscriptionDTO_NullPrice() {
        Subscription subscription = new Subscription();
        subscription.setPrice(null);

        SubscriptionDTO dto = mapper.toSubscriptionDTO(subscription);

        assertNotNull(dto);
        assertNull(dto.getPrice());
    }

    @Test
    void toSubscriptionDTOList_NullInput_ReturnsNull() {
        assertNull(mapper.toSubscriptionDTOList(null));
    }

    @Test
    void toSubscriptionDTOList_EmptyList_ReturnsEmptyList() {
        List<SubscriptionDTO> dtos = mapper.toSubscriptionDTOList(Collections.emptyList());
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toSubscriptionDTOList_NonEmptyList() {
        Subscription s1 = new Subscription();
        s1.setId(UUID.randomUUID());
        Subscription s2 = new Subscription();
        s2.setId(UUID.randomUUID());

        List<SubscriptionDTO> dtos = mapper.toSubscriptionDTOList(Arrays.asList(s1, s2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(s1.getId(), dtos.get(0).getId());
        assertEquals(s2.getId(), dtos.get(1).getId());
    }

    @Test
    void durationEnumToSubscriptionDuration_ValidValues() {
        assertEquals(SubscriptionDuration.WEEK, mapper.durationEnumToSubscriptionDuration(SubscriptionCreateDTO.DurationEnum.WEEK));
        assertEquals(SubscriptionDuration.MONTH, mapper.durationEnumToSubscriptionDuration(SubscriptionCreateDTO.DurationEnum.MONTH));
        assertEquals(SubscriptionDuration.YEAR, mapper.durationEnumToSubscriptionDuration(SubscriptionCreateDTO.DurationEnum.YEAR));
    }

    @Test
    void durationEnumToSubscriptionDuration_NullValue() {
        assertNull(mapper.durationEnumToSubscriptionDuration(null));
    }

    @Test
    void durationEnumToSubscriptionDuration1_ValidValues() {
        assertEquals(SubscriptionDuration.WEEK, mapper.durationEnumToSubscriptionDuration1(SubscriptionUpdateDTO.DurationEnum.WEEK));
        assertEquals(SubscriptionDuration.MONTH, mapper.durationEnumToSubscriptionDuration1(SubscriptionUpdateDTO.DurationEnum.MONTH));
        assertEquals(SubscriptionDuration.YEAR, mapper.durationEnumToSubscriptionDuration1(SubscriptionUpdateDTO.DurationEnum.YEAR));
    }

    @Test
    void durationEnumToSubscriptionDuration1_NullValue() {
        assertNull(mapper.durationEnumToSubscriptionDuration1(null));
    }

    @Test
    void subscriptionDurationToDurationEnum_ValidValues() {
        assertEquals(SubscriptionDTO.DurationEnum.WEEK, mapper.subscriptionDurationToDurationEnum(SubscriptionDuration.WEEK));
        assertEquals(SubscriptionDTO.DurationEnum.MONTH, mapper.subscriptionDurationToDurationEnum(SubscriptionDuration.MONTH));
        assertEquals(SubscriptionDTO.DurationEnum.YEAR, mapper.subscriptionDurationToDurationEnum(SubscriptionDuration.YEAR));
    }

    @Test
    void subscriptionDurationToDurationEnum_NullValue() {
        assertNull(mapper.subscriptionDurationToDurationEnum(null));
    }
}

