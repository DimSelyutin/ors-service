package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for entity UserSubscription.
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserSubscriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscription", expression = "java(toSubscription(createDTO.getSubscriptionId()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    UserSubscription toEntity(UserSubscriptionCreateDTO createDTO);

    @Mapping(target = "subscriptionId", source = "subscription.id")
    UserSubscriptionDTO toDto(UserSubscription entity);

    List<UserSubscriptionDTO> toDtos(List<UserSubscription> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void update(@MappingTarget UserSubscription entity, UserSubscriptionUpdateDTO updateDTO);

    default Subscription toSubscription(java.util.UUID id) {
        if (id == null) return null;
        Subscription s = new Subscription();
        s.setId(id);
        return s;
    }
}


