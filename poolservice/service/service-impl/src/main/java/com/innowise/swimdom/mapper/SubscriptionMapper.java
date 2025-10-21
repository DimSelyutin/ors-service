package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for entity Subscription.
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SubscriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Subscription toEntity(SubscriptionCreateDTO dto);

    SubscriptionDTO toDto(Subscription entity);

    List<SubscriptionDTO> toDtos(List<Subscription> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void update(@MappingTarget Subscription entity, SubscriptionUpdateDTO dto);
}


