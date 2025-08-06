package com.innowise.swimdom.mapper;

import com.innowise.swimdom.entity.Booking;
import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.openapi.model.BookingResponseDTO;
import com.innowise.swimdom.openapi.model.BookingUpdateRequestDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for mapping Booking and Dto.
 */
@Mapper(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, imports = {LocalDateTime.class})
public interface BookingMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userSubscriptionId", source = "userSubscription.id")
    @Mapping(target = "scheduleId", source = "schedule.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    BookingResponseDTO toBookingResponseDTO(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "userSubscription.id", source = "userSubscriptionId")
    @Mapping(target = "schedule.id", source = "scheduleId")
    @Mapping(target = "status", constant = "CONFIRMED")
    @Mapping(target = "notificationSent", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Booking toBooking(BookingCreateRequestDTO bookingCreateRequestDTO);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "userSubscription.id", source = "userSubscriptionId")
    @Mapping(target = "schedule.id", source = "scheduleId")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdAt", ignore = true)
    void updateBookingFromDTO(BookingUpdateRequestDTO bookingUpdateRequestDTO, @MappingTarget Booking booking);

    List<BookingResponseDTO> toBookingResponseDTOList(List<Booking> bookings);

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateTimeString) {
        if (dateTimeString == null) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
