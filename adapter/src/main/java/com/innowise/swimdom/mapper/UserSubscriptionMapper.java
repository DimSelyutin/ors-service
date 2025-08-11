package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
<<<<<<< Updated upstream
import org.mapstruct.ReportingPolicy;
=======
import org.mapstruct.NullValuePropertyMappingStrategy;
>>>>>>> Stashed changes

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for entity {@link com.innowise.swimdom.entity.Pool}.
 *
 * @author DimSelyutin
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
<<<<<<< Updated upstream
        unmappedTargetPolicy = ReportingPolicy.ERROR)
=======
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
>>>>>>> Stashed changes
public interface UserSubscriptionMapper {

    /**
     * Converts an entity to a DTO.
     *
     * @param userSubscription entity
     * @return DTO
     */
<<<<<<< Updated upstream
    @Mapping(target = "userId", source = "userSubscription.user.id")
    @Mapping(target = "subscriptionId", source = "userSubscription.subscription.id")
=======
>>>>>>> Stashed changes
    UserSubscriptionDTO toUserSubscriptionDto(UserSubscription userSubscription);

    /**
     * Converts a list of entities into a list of DTOs.
     *
     * @param userSubscriptions list of entities
     * @return list of DTOs
     */
    List<UserSubscriptionDTO> toUserSubscriptionDtoList(List<UserSubscription> userSubscriptions);

    /**
     * Converts an DTO to entity.
     *
     * @param userSubscriptionDTO dto
     * @return DTO
     */
    @Mapping(target = "id", ignore = true)
<<<<<<< Updated upstream
    @Mapping(target = "user.id", source = "userSubscriptionDTO.userId")
    @Mapping(target = "subscription.id", source = "userSubscriptionDTO.subscriptionId")
=======
>>>>>>> Stashed changes
    UserSubscription toUserSubscription(UserSubscriptionDTO userSubscriptionDTO);

    /**
     * Converts an DTO to entity.
     *
     * @param userSubscriptionDTO dto
     * @return DTO
     */
<<<<<<< Updated upstream
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userSubscriptionDTO.userId")
    @Mapping(target = "subscription.id", source = "userSubscriptionDTO.subscriptionId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
=======
>>>>>>> Stashed changes
    UserSubscription toUserSubscription(UserSubscriptionCreateDTO userSubscriptionDTO);

    /**
     * Update entity from DTO.
     *
     * @params entity, updateDTO
     */
<<<<<<< Updated upstream
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entity.user.id", ignore = true)
    @Mapping(target = "entity.subscription.id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
=======
>>>>>>> Stashed changes
    void updateUserSubscriptionFromDto(UserSubscriptionUpdateDTO updateDTO, @MappingTarget UserSubscription entity);
}
