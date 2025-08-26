package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Booking;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.enums.BookingStatus;
import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.openapi.model.BookingResponseDTO;
import com.innowise.swimdom.openapi.model.BookingUpdateRequestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T18:03:59+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingResponseDTO toBookingResponseDTO(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();

        bookingResponseDTO.setUserId( bookingUserId( booking ) );
        bookingResponseDTO.setUserSubscriptionId( bookingUserSubscriptionId( booking ) );
        bookingResponseDTO.setScheduleId( bookingScheduleId( booking ) );
        bookingResponseDTO.setStatus( bookingStatusToStatusEnum( booking.getStatus() ) );
        bookingResponseDTO.setId( booking.getId() );
        bookingResponseDTO.setBookingDatetime( booking.getBookingDatetime() );
        bookingResponseDTO.setNotificationSent( booking.getNotificationSent() );
        bookingResponseDTO.setCreatedAt( booking.getCreatedAt() );

        bookingResponseDTO.setUpdatedAt( java.time.LocalDateTime.now() );

        return bookingResponseDTO;
    }

    @Override
    public Booking toBooking(BookingCreateRequestDTO bookingCreateRequestDTO) {
        if ( bookingCreateRequestDTO == null ) {
            return null;
        }

        Booking.BookingBuilder booking = Booking.builder();

        booking.user( bookingCreateRequestDTOToUser( bookingCreateRequestDTO ) );
        booking.userSubscription( bookingCreateRequestDTOToUserSubscription( bookingCreateRequestDTO ) );
        booking.schedule( bookingCreateRequestDTOToSchedule( bookingCreateRequestDTO ) );
        booking.bookingDatetime( bookingCreateRequestDTO.getBookingDatetime() );

        booking.status( BookingStatus.CONFIRMED );
        booking.notificationSent( false );
        booking.updatedAt( java.time.LocalDateTime.now() );

        return booking.build();
    }

    @Override
    public void updateBookingFromDTO(BookingUpdateRequestDTO bookingUpdateRequestDTO, Booking booking) {
        if ( bookingUpdateRequestDTO == null ) {
            return;
        }

        if ( booking.getUser() == null ) {
            booking.setUser( new User() );
        }
        bookingUpdateRequestDTOToUser( bookingUpdateRequestDTO, booking.getUser() );
        if ( booking.getUserSubscription() == null ) {
            booking.setUserSubscription( new UserSubscription() );
        }
        bookingUpdateRequestDTOToUserSubscription( bookingUpdateRequestDTO, booking.getUserSubscription() );
        if ( booking.getSchedule() == null ) {
            booking.setSchedule( new Schedule() );
        }
        bookingUpdateRequestDTOToSchedule( bookingUpdateRequestDTO, booking.getSchedule() );
        booking.setId( bookingUpdateRequestDTO.getId() );
        booking.setStatus( statusEnumToBookingStatus( bookingUpdateRequestDTO.getStatus() ) );
        booking.setBookingDatetime( bookingUpdateRequestDTO.getBookingDatetime() );
        booking.setNotificationSent( bookingUpdateRequestDTO.getNotificationSent() );

        booking.setUpdatedAt( java.time.LocalDateTime.now() );
    }

    @Override
    public List<BookingResponseDTO> toBookingResponseDTOList(List<Booking> bookings) {
        if ( bookings == null ) {
            return null;
        }

        List<BookingResponseDTO> list = new ArrayList<BookingResponseDTO>( bookings.size() );
        for ( Booking booking : bookings ) {
            list.add( toBookingResponseDTO( booking ) );
        }

        return list;
    }

    private UUID bookingUserId(Booking booking) {
        User user = booking.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private UUID bookingUserSubscriptionId(Booking booking) {
        UserSubscription userSubscription = booking.getUserSubscription();
        if ( userSubscription == null ) {
            return null;
        }
        return userSubscription.getId();
    }

    private UUID bookingScheduleId(Booking booking) {
        Schedule schedule = booking.getSchedule();
        if ( schedule == null ) {
            return null;
        }
        return schedule.getId();
    }

    protected BookingResponseDTO.StatusEnum bookingStatusToStatusEnum(BookingStatus bookingStatus) {
        if ( bookingStatus == null ) {
            return null;
        }

        BookingResponseDTO.StatusEnum statusEnum;

        switch ( bookingStatus ) {
            case PENDING: statusEnum = BookingResponseDTO.StatusEnum.PENDING;
            break;
            case CONFIRMED: statusEnum = BookingResponseDTO.StatusEnum.CONFIRMED;
            break;
            case CANCELLED: statusEnum = BookingResponseDTO.StatusEnum.CANCELLED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + bookingStatus );
        }

        return statusEnum;
    }

    protected User bookingCreateRequestDTOToUser(BookingCreateRequestDTO bookingCreateRequestDTO) {
        if ( bookingCreateRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( bookingCreateRequestDTO.getUserId() );

        return user;
    }

    protected UserSubscription bookingCreateRequestDTOToUserSubscription(BookingCreateRequestDTO bookingCreateRequestDTO) {
        if ( bookingCreateRequestDTO == null ) {
            return null;
        }

        UserSubscription userSubscription = new UserSubscription();

        userSubscription.setId( bookingCreateRequestDTO.getUserSubscriptionId() );

        return userSubscription;
    }

    protected Schedule bookingCreateRequestDTOToSchedule(BookingCreateRequestDTO bookingCreateRequestDTO) {
        if ( bookingCreateRequestDTO == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        schedule.setId( bookingCreateRequestDTO.getScheduleId() );

        return schedule;
    }

    protected void bookingUpdateRequestDTOToUser(BookingUpdateRequestDTO bookingUpdateRequestDTO, User mappingTarget) {
        if ( bookingUpdateRequestDTO == null ) {
            return;
        }

        mappingTarget.setId( bookingUpdateRequestDTO.getUserId() );
    }

    protected void bookingUpdateRequestDTOToUserSubscription(BookingUpdateRequestDTO bookingUpdateRequestDTO, UserSubscription mappingTarget) {
        if ( bookingUpdateRequestDTO == null ) {
            return;
        }

        mappingTarget.setId( bookingUpdateRequestDTO.getUserSubscriptionId() );
    }

    protected void bookingUpdateRequestDTOToSchedule(BookingUpdateRequestDTO bookingUpdateRequestDTO, Schedule mappingTarget) {
        if ( bookingUpdateRequestDTO == null ) {
            return;
        }

        mappingTarget.setId( bookingUpdateRequestDTO.getScheduleId() );
    }

    protected BookingStatus statusEnumToBookingStatus(BookingUpdateRequestDTO.StatusEnum statusEnum) {
        if ( statusEnum == null ) {
            return null;
        }

        BookingStatus bookingStatus;

        switch ( statusEnum ) {
            case PENDING: bookingStatus = BookingStatus.PENDING;
            break;
            case CONFIRMED: bookingStatus = BookingStatus.CONFIRMED;
            break;
            case CANCELLED: bookingStatus = BookingStatus.CANCELLED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + statusEnum );
        }

        return bookingStatus;
    }
}
