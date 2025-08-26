package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T18:03:58+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class UserSubscriptionMapperImpl implements UserSubscriptionMapper {

    @Override
    public UserSubscriptionDTO toUserSubscriptionDto(UserSubscription userSubscription) {
        if ( userSubscription == null ) {
            return null;
        }

        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();

        userSubscriptionDTO.setUserId( userSubscriptionUserId( userSubscription ) );
        userSubscriptionDTO.setSubscriptionId( userSubscriptionSubscriptionId( userSubscription ) );
        userSubscriptionDTO.setId( userSubscription.getId() );
        userSubscriptionDTO.setEstimate( userSubscription.getEstimate() );
        userSubscriptionDTO.setStartDate( userSubscription.getStartDate() );
        userSubscriptionDTO.setEndDate( userSubscription.getEndDate() );
        userSubscriptionDTO.setCreatedAt( userSubscription.getCreatedAt() );
        userSubscriptionDTO.setUpdatedAt( userSubscription.getUpdatedAt() );

        return userSubscriptionDTO;
    }

    @Override
    public List<UserSubscriptionDTO> toUserSubscriptionDtoList(List<UserSubscription> userSubscriptions) {
        if ( userSubscriptions == null ) {
            return null;
        }

        List<UserSubscriptionDTO> list = new ArrayList<UserSubscriptionDTO>( userSubscriptions.size() );
        for ( UserSubscription userSubscription : userSubscriptions ) {
            list.add( toUserSubscriptionDto( userSubscription ) );
        }

        return list;
    }

    @Override
    public UserSubscription toUserSubscription(UserSubscriptionDTO userSubscriptionDTO) {
        if ( userSubscriptionDTO == null ) {
            return null;
        }

        UserSubscription userSubscription = new UserSubscription();

        userSubscription.setUser( userSubscriptionDTOToUser( userSubscriptionDTO ) );
        userSubscription.setSubscription( userSubscriptionDTOToSubscription( userSubscriptionDTO ) );
        userSubscription.setEstimate( userSubscriptionDTO.getEstimate() );
        userSubscription.setStartDate( userSubscriptionDTO.getStartDate() );
        userSubscription.setEndDate( userSubscriptionDTO.getEndDate() );
        userSubscription.setCreatedAt( userSubscriptionDTO.getCreatedAt() );
        userSubscription.setUpdatedAt( userSubscriptionDTO.getUpdatedAt() );

        return userSubscription;
    }

    @Override
    public UserSubscription toUserSubscription(UserSubscriptionCreateDTO userSubscriptionDTO) {
        if ( userSubscriptionDTO == null ) {
            return null;
        }

        UserSubscription userSubscription = new UserSubscription();

        userSubscription.setUser( userSubscriptionCreateDTOToUser( userSubscriptionDTO ) );
        userSubscription.setSubscription( userSubscriptionCreateDTOToSubscription( userSubscriptionDTO ) );
        userSubscription.setEstimate( userSubscriptionDTO.getEstimate() );
        userSubscription.setStartDate( userSubscriptionDTO.getStartDate() );
        userSubscription.setEndDate( userSubscriptionDTO.getEndDate() );

        userSubscription.setUpdatedAt( java.time.LocalDateTime.now() );

        return userSubscription;
    }

    @Override
    public void updateUserSubscriptionFromDto(UserSubscriptionUpdateDTO updateDTO, UserSubscription entity) {
        if ( updateDTO == null ) {
            return;
        }

        entity.setEstimate( updateDTO.getEstimate() );
        entity.setStartDate( updateDTO.getStartDate() );
        entity.setEndDate( updateDTO.getEndDate() );
        if ( entity.getUser() == null ) {
            entity.setUser( new User() );
        }
        userSubscriptionUpdateDTOToUser( updateDTO, entity.getUser() );
        if ( entity.getSubscription() == null ) {
            entity.setSubscription( new Subscription() );
        }
        userSubscriptionUpdateDTOToSubscription( updateDTO, entity.getSubscription() );

        entity.setUpdatedAt( java.time.LocalDateTime.now() );
    }

    private UUID userSubscriptionUserId(UserSubscription userSubscription) {
        User user = userSubscription.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private UUID userSubscriptionSubscriptionId(UserSubscription userSubscription) {
        Subscription subscription = userSubscription.getSubscription();
        if ( subscription == null ) {
            return null;
        }
        return subscription.getId();
    }

    protected User userSubscriptionDTOToUser(UserSubscriptionDTO userSubscriptionDTO) {
        if ( userSubscriptionDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userSubscriptionDTO.getUserId() );

        return user;
    }

    protected Subscription userSubscriptionDTOToSubscription(UserSubscriptionDTO userSubscriptionDTO) {
        if ( userSubscriptionDTO == null ) {
            return null;
        }

        Subscription subscription = new Subscription();

        subscription.setId( userSubscriptionDTO.getSubscriptionId() );

        return subscription;
    }

    protected User userSubscriptionCreateDTOToUser(UserSubscriptionCreateDTO userSubscriptionCreateDTO) {
        if ( userSubscriptionCreateDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userSubscriptionCreateDTO.getUserId() );

        return user;
    }

    protected Subscription userSubscriptionCreateDTOToSubscription(UserSubscriptionCreateDTO userSubscriptionCreateDTO) {
        if ( userSubscriptionCreateDTO == null ) {
            return null;
        }

        Subscription subscription = new Subscription();

        subscription.setId( userSubscriptionCreateDTO.getSubscriptionId() );

        return subscription;
    }

    protected void userSubscriptionUpdateDTOToUser(UserSubscriptionUpdateDTO userSubscriptionUpdateDTO, User mappingTarget) {
        if ( userSubscriptionUpdateDTO == null ) {
            return;
        }
    }

    protected void userSubscriptionUpdateDTOToSubscription(UserSubscriptionUpdateDTO userSubscriptionUpdateDTO, Subscription mappingTarget) {
        if ( userSubscriptionUpdateDTO == null ) {
            return;
        }
    }
}
