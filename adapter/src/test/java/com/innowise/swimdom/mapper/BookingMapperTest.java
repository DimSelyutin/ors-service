package com.innowise.swimdom.mapper;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private BookingMapper bookingMapper;

    // Test UUIDs
    private static final UUID BOOKING_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID USER_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    private static final UUID POOL_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
    private static final UUID SCHEDULE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
    private static final UUID USER_SUBSCRIPTION_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");

    // Test dates and times
    private static final LocalDateTime BOOKING_DATETIME = LocalDateTime.parse("2024-06-21T17:00:00");
    private static final LocalDateTime SCHEDULE_START = LocalDateTime.parse("2024-06-21T17:00:00");
    private static final LocalDateTime SCHEDULE_END = LocalDateTime.parse("2024-06-21T18:00:00");
    private static final LocalDate SUBSCRIPTION_START = LocalDate.parse("2024-01-01");
    private static final LocalDate SUBSCRIPTION_END = LocalDate.parse("2024-12-31");
    private static final LocalDateTime CREATED_AT = LocalDateTime.parse("2024-06-21T16:00:00");
    private static final LocalDateTime UPDATED_AT = LocalDateTime.parse("2024-06-21T16:00:00");

    @BeforeEach
    void setUp() {
        bookingMapper = Mappers.getMapper(BookingMapper.class);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail("test@example.com");
        user.setName("Test User");
        return user;
    }

    private Pool createTestPool() {
        Pool pool = new Pool();
        pool.setId(POOL_ID);
        pool.setName("Test Pool");
        pool.setCapacity(50);
        return pool;
    }

    private Schedule createTestSchedule() {
        Schedule schedule = new Schedule();
        schedule.setId(SCHEDULE_ID);
        schedule.setPool(createTestPool());
        schedule.setStartDatetime(SCHEDULE_START);
        schedule.setEndDatetime(SCHEDULE_END);
        return schedule;
    }

    private UserSubscription createTestUserSubscription() {
        UserSubscription subscription = new UserSubscription();
        subscription.setId(USER_SUBSCRIPTION_ID);
        subscription.setUser(createTestUser());
        subscription.setStartDate(SUBSCRIPTION_START);
        subscription.setEndDate(SUBSCRIPTION_END);
        return subscription;
    }

    private Booking createTestBooking() {
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

    private BookingCreateRequestDTO createTestBookingCreateRequestDTO() {
        BookingCreateRequestDTO dto = new BookingCreateRequestDTO();
        dto.setUserId(USER_ID);
        dto.setScheduleId(SCHEDULE_ID);
        dto.setUserSubscriptionId(USER_SUBSCRIPTION_ID);
        dto.setBookingDatetime(BOOKING_DATETIME);
        return dto;
    }

    private BookingUpdateRequestDTO createTestBookingUpdateRequestDTO() {
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

    @Test
    void toBookingResponseDTO_Success() {
        // GIVEN
        Booking booking = createTestBooking();

        // WHEN
        BookingResponseDTO result = bookingMapper.toBookingResponseDTO(booking);

        // THEN
        assertNotNull(result);
        assertEquals(BOOKING_ID, result.getId());
        assertEquals(USER_ID, result.getUserId());
        assertEquals(SCHEDULE_ID, result.getScheduleId());
        assertEquals(USER_SUBSCRIPTION_ID, result.getUserSubscriptionId());
        assertEquals(BookingResponseDTO.StatusEnum.CONFIRMED, result.getStatus());
        assertEquals(BOOKING_DATETIME, result.getBookingDatetime());
        assertFalse(result.getNotificationSent());
        assertEquals(CREATED_AT, result.getCreatedAt());
        assertEquals(UPDATED_AT, result.getUpdatedAt());
    }

    @Test
    void toBookingResponseDTO_NullInput_ReturnsNull() {
        // WHEN
        BookingResponseDTO result = bookingMapper.toBookingResponseDTO(null);

        // THEN
        assertNull(result);
    }

    @Test
    void toBooking_Success() {
        // GIVEN
        BookingCreateRequestDTO createRequest = createTestBookingCreateRequestDTO();

        // WHEN
        Booking result = bookingMapper.toBooking(createRequest);

        // THEN
        assertNotNull(result);
        assertEquals(USER_ID, result.getUser().getId());
        assertEquals(SCHEDULE_ID, result.getSchedule().getId());
        assertEquals(USER_SUBSCRIPTION_ID, result.getUserSubscription().getId());
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(BOOKING_DATETIME, result.getBookingDatetime());
        assertFalse(result.getNotificationSent());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void toBooking_NullInput_ReturnsNull() {
        // WHEN
        Booking result = bookingMapper.toBooking(null);

        // THEN
        assertNull(result);
    }

    @Test
    void updateBookingFromDTO_Success() {
        // GIVEN
        BookingUpdateRequestDTO updateRequest = createTestBookingUpdateRequestDTO();
        Booking existingBooking = createTestBooking();
        LocalDateTime originalUpdatedAt = existingBooking.getUpdatedAt();

        // WHEN
        bookingMapper.updateBookingFromDTO(updateRequest, existingBooking);

        // THEN
        assertEquals(USER_ID, existingBooking.getUser().getId());
        assertEquals(SCHEDULE_ID, existingBooking.getSchedule().getId());
        assertEquals(USER_SUBSCRIPTION_ID, existingBooking.getUserSubscription().getId());
        assertEquals(BookingStatus.CONFIRMED, existingBooking.getStatus());
        assertEquals(BOOKING_DATETIME, existingBooking.getBookingDatetime());
        assertFalse(existingBooking.getNotificationSent());
        assertNotEquals(originalUpdatedAt, existingBooking.getUpdatedAt());
    }

    @Test
    void updateBookingFromDTO_NullDTO_DoesNotUpdate() {
        // GIVEN
        Booking existingBooking = createTestBooking();
        LocalDateTime originalUpdatedAt = existingBooking.getUpdatedAt();

        // WHEN
        bookingMapper.updateBookingFromDTO(null, existingBooking);

        // THEN
        assertEquals(originalUpdatedAt, existingBooking.getUpdatedAt());
    }

    @Test
    void toBookingResponseDTOList_Success() {
        // GIVEN
        List<Booking> bookings = List.of(createTestBooking());

        // WHEN
        List<BookingResponseDTO> result = bookingMapper.toBookingResponseDTOList(bookings);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BOOKING_ID, result.get(0).getId());
        assertEquals(USER_ID, result.get(0).getUserId());
    }

    @Test
    void toBookingResponseDTOList_EmptyList_ReturnsEmptyList() {
        // GIVEN
        List<Booking> bookings = List.of();

        // WHEN
        List<BookingResponseDTO> result = bookingMapper.toBookingResponseDTOList(bookings);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toBookingResponseDTOList_NullInput_ReturnsNull() {
        // WHEN
        List<BookingResponseDTO> result = bookingMapper.toBookingResponseDTOList(null);

        // THEN
        assertNull(result);
    }

    @Test
    void localDateTimeToString_Success() {
        // GIVEN
        LocalDateTime dateTime = LocalDateTime.parse("2024-06-21T17:00:00");

        // WHEN
        String result = bookingMapper.localDateTimeToString(dateTime);

        // THEN
        assertEquals("2024-06-21T17:00:00", result);
    }

    @Test
    void localDateTimeToString_NullInput_ReturnsNull() {
        // WHEN
        String result = bookingMapper.localDateTimeToString(null);

        // THEN
        assertNull(result);
    }

    @Test
    void stringToLocalDateTime_Success() {
        // GIVEN
        String dateTimeString = "2024-06-21T17:00:00";

        // WHEN
        LocalDateTime result = bookingMapper.stringToLocalDateTime(dateTimeString);

        // THEN
        assertEquals(LocalDateTime.parse("2024-06-21T17:00:00"), result);
    }

    @Test
    void stringToLocalDateTime_NullInput_ReturnsNull() {
        // WHEN
        LocalDateTime result = bookingMapper.stringToLocalDateTime(null);

        // THEN
        assertNull(result);
    }
} 