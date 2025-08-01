package com.innowise.swimdom.service.util;

import com.innowise.swimdom.entity.Booking;
import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.enums.BookingStatus;
import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.openapi.model.BookingResponseDTO;
import com.innowise.swimdom.openapi.model.BookingUpdateRequestDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class BookingTestData {

    public static final LocalDateTime FIXED_NOW = LocalDateTime.now();
    public static final LocalDate FIXED_TODAY = FIXED_NOW.toLocalDate();

    public static final UUID BOOKING_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    public static final UUID USER_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    public static final UUID POOL_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
    public static final UUID SCHEDULE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
    public static final UUID USER_SUBSCRIPTION_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");

    public static final LocalDateTime BOOKING_DATETIME = FIXED_NOW.plusHours(4);
    public static final LocalDateTime SCHEDULE_START = LocalDateTime.of(FIXED_TODAY, LocalTime.of(17, 0)); // 2024-06-21T17:00:00
    public static final LocalDateTime SCHEDULE_END = LocalDateTime.of(FIXED_TODAY, LocalTime.of(18, 0));   // 2024-06-21T18:00:00
    public static final LocalDate SUBSCRIPTION_START = FIXED_TODAY;                      // 2024-06-21
    public static final LocalDate SUBSCRIPTION_END = FIXED_TODAY.plusMonths(1);          // 2024-07-21
    public static final LocalDateTime CREATED_AT = FIXED_NOW;                           // 2024-06-21T12:00:00
    public static final LocalDateTime UPDATED_AT = FIXED_NOW;                           // 2024-06-21T12:00:00

    public static final String USER_EMAIL = "test@example.com";
    public static final String USER_NAME = "Test User";
    public static final String POOL_NAME = "Test Pool";
    public static final int POOL_CAPACITY = 50;
    public static final short WEEKDAY_MONDAY = 4;
    public static final LocalTime OPEN_TIME = LocalTime.of(8, 0);  // 08:00
    public static final LocalTime CLOSE_TIME = LocalTime.of(22, 0); // 22:00


    public static User createTestUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setName(USER_NAME);
        return user;
    }

    public static Pool createTestPool() {
        Pool pool = new Pool();
        pool.setId(POOL_ID);
        pool.setName(POOL_NAME);
        pool.setCapacity(POOL_CAPACITY);
        return pool;
    }

    public static PoolWorkingHours createTestPoolWorkingHours() {
        PoolWorkingHours workingHours = new PoolWorkingHours();
        workingHours.setWeekday(WEEKDAY_MONDAY);
        workingHours.setOpenTime(OPEN_TIME);
        workingHours.setCloseTime(CLOSE_TIME);
        return workingHours;
    }

    public static Schedule createTestSchedule() {
        Schedule schedule = new Schedule();
        schedule.setId(SCHEDULE_ID);
        schedule.setPool(createTestPool());
        schedule.setStartDatetime(SCHEDULE_START);
        schedule.setEndDatetime(SCHEDULE_END);
        return schedule;
    }

    public static UserSubscription createTestUserSubscription() {
        UserSubscription subscription = new UserSubscription();
        subscription.setId(USER_SUBSCRIPTION_ID);
        subscription.setUser(createTestUser());
        subscription.setStartDate(SUBSCRIPTION_START);
        subscription.setEndDate(SUBSCRIPTION_END);
        return subscription;
    }

    public static Booking createTestBooking() {
        Booking booking = new Booking();
        booking.setId(BOOKING_ID);
        booking.setUser(createTestUser());
        booking.setUserSubscription(createTestUserSubscription());
        booking.setSchedule(createTestSchedule());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookingDatetime(BOOKING_DATETIME);
        booking.setNotificationSent(false);
        booking.setCreatedAt(CREATED_AT);
        booking.setUpdatedAt(UPDATED_AT);
        return booking;
    }

    public static BookingCreateRequestDTO createTestBookingCreateRequestDTO() {
        BookingCreateRequestDTO dto = new BookingCreateRequestDTO();
        dto.setUserId(USER_ID);
        dto.setScheduleId(SCHEDULE_ID);
        dto.setUserSubscriptionId(USER_SUBSCRIPTION_ID);
        dto.setBookingDatetime(BOOKING_DATETIME);
        return dto;
    }

    public static BookingUpdateRequestDTO createTestBookingUpdateRequestDTO() {
        BookingUpdateRequestDTO dto = new BookingUpdateRequestDTO();
        dto.setId(BOOKING_ID);
        dto.setUserId(USER_ID);
        dto.setScheduleId(SCHEDULE_ID);
        dto.setUserSubscriptionId(USER_SUBSCRIPTION_ID);
        dto.setStatus(BookingUpdateRequestDTO.StatusEnum.CONFIRMED);
        dto.setBookingDatetime(BOOKING_DATETIME);
        dto.setNotificationSent(false);
        return dto;
    }

    public static BookingResponseDTO createTestBookingResponseDTO() {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(BOOKING_ID);
        dto.setUserId(USER_ID);
        dto.setScheduleId(SCHEDULE_ID);
        dto.setUserSubscriptionId(USER_SUBSCRIPTION_ID);
        dto.setStatus(BookingResponseDTO.StatusEnum.CONFIRMED);
        dto.setBookingDatetime(BOOKING_DATETIME);
        dto.setNotificationSent(false);
        dto.setCreatedAt(CREATED_AT);
        dto.setUpdatedAt(UPDATED_AT);
        return dto;
    }
} 