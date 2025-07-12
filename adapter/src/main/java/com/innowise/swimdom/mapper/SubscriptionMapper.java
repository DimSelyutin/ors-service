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
 * Mapper for entity {@link com.innowise.swimdom.entity.Pool}.
 *
 * @author DimSelyutin
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SubscriptionMapper {

    /**
     * Mapping SubscriptionCreateDTO to Subscription entity.
     * Ignoring id, assuming it is generated in the database.
     *
     * @param createDTO a subscription creation object
     * @return Subscription entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Subscription toSubscriptionEntity(SubscriptionCreateDTO createDTO);

    /**
     * Mapping SubscriptionUpdateDTO to Subscription entity.
     * Used to update an existing entity.
     *
     * @param updateDTO            update data
     * @param existingSubscription existingSubscription entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateSubscriptionFromDTO(SubscriptionUpdateDTO updateDTO, @MappingTarget Subscription existingSubscription);

    /**
     * Mapping Subscription entity to SubscriptionDTO.
     *
     * @param subscription entity
     * @return SubscriptionDTO
     */
    SubscriptionDTO toSubscriptionDTO(Subscription subscription);

    /**
     * Mapping List of Subscription entities to List of SubscriptionDTO.
     *
     * @param subscriptions list of entities
     * @return list of DTOs
     */
    List<SubscriptionDTO> toSubscriptionDTOList(List<Subscription> subscriptions);
}
