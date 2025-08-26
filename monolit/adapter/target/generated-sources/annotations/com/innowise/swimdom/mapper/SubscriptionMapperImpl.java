package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.enums.SubscriptionDuration;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T18:03:59+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {

    @Override
    public Subscription toSubscriptionEntity(SubscriptionCreateDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        Subscription subscription = new Subscription();

        subscription.setName( createDTO.getName() );
        subscription.setDescription( createDTO.getDescription() );
        subscription.setDuration( durationEnumToSubscriptionDuration( createDTO.getDuration() ) );
        if ( createDTO.getPrice() != null ) {
            subscription.setPrice( BigDecimal.valueOf( createDTO.getPrice() ) );
        }

        subscription.setUpdatedAt( java.time.LocalDateTime.now() );

        return subscription;
    }

    @Override
    public void updateSubscriptionFromDTO(SubscriptionUpdateDTO updateDTO, Subscription existingSubscription) {
        if ( updateDTO == null ) {
            return;
        }

        existingSubscription.setName( updateDTO.getName() );
        existingSubscription.setDescription( updateDTO.getDescription() );
        existingSubscription.setDuration( durationEnumToSubscriptionDuration1( updateDTO.getDuration() ) );
        if ( updateDTO.getPrice() != null ) {
            existingSubscription.setPrice( BigDecimal.valueOf( updateDTO.getPrice() ) );
        }
        else {
            existingSubscription.setPrice( null );
        }

        existingSubscription.setUpdatedAt( java.time.LocalDateTime.now() );
    }

    @Override
    public SubscriptionDTO toSubscriptionDTO(Subscription subscription) {
        if ( subscription == null ) {
            return null;
        }

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();

        subscriptionDTO.setId( subscription.getId() );
        subscriptionDTO.setName( subscription.getName() );
        subscriptionDTO.setDescription( subscription.getDescription() );
        subscriptionDTO.setDuration( subscriptionDurationToDurationEnum( subscription.getDuration() ) );
        if ( subscription.getPrice() != null ) {
            subscriptionDTO.setPrice( subscription.getPrice().doubleValue() );
        }
        subscriptionDTO.setCreatedAt( subscription.getCreatedAt() );
        subscriptionDTO.setUpdatedAt( subscription.getUpdatedAt() );

        return subscriptionDTO;
    }

    @Override
    public List<SubscriptionDTO> toSubscriptionDTOList(List<Subscription> subscriptions) {
        if ( subscriptions == null ) {
            return null;
        }

        List<SubscriptionDTO> list = new ArrayList<SubscriptionDTO>( subscriptions.size() );
        for ( Subscription subscription : subscriptions ) {
            list.add( toSubscriptionDTO( subscription ) );
        }

        return list;
    }

    protected SubscriptionDuration durationEnumToSubscriptionDuration(SubscriptionCreateDTO.DurationEnum durationEnum) {
        if ( durationEnum == null ) {
            return null;
        }

        SubscriptionDuration subscriptionDuration;

        switch ( durationEnum ) {
            case WEEK: subscriptionDuration = SubscriptionDuration.WEEK;
            break;
            case MONTH: subscriptionDuration = SubscriptionDuration.MONTH;
            break;
            case YEAR: subscriptionDuration = SubscriptionDuration.YEAR;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + durationEnum );
        }

        return subscriptionDuration;
    }

    protected SubscriptionDuration durationEnumToSubscriptionDuration1(SubscriptionUpdateDTO.DurationEnum durationEnum) {
        if ( durationEnum == null ) {
            return null;
        }

        SubscriptionDuration subscriptionDuration;

        switch ( durationEnum ) {
            case WEEK: subscriptionDuration = SubscriptionDuration.WEEK;
            break;
            case MONTH: subscriptionDuration = SubscriptionDuration.MONTH;
            break;
            case YEAR: subscriptionDuration = SubscriptionDuration.YEAR;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + durationEnum );
        }

        return subscriptionDuration;
    }

    protected SubscriptionDTO.DurationEnum subscriptionDurationToDurationEnum(SubscriptionDuration subscriptionDuration) {
        if ( subscriptionDuration == null ) {
            return null;
        }

        SubscriptionDTO.DurationEnum durationEnum;

        switch ( subscriptionDuration ) {
            case WEEK: durationEnum = SubscriptionDTO.DurationEnum.WEEK;
            break;
            case MONTH: durationEnum = SubscriptionDTO.DurationEnum.MONTH;
            break;
            case YEAR: durationEnum = SubscriptionDTO.DurationEnum.YEAR;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + subscriptionDuration );
        }

        return durationEnum;
    }
}
