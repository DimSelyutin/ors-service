package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Booking;
import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.exception.BookingNotFoundException;
import com.innowise.swimdom.exception.ScheduleNotFoundException;
import com.innowise.swimdom.exception.UserNotFoundException;
import com.innowise.swimdom.exception.UserSubscriptionNotFoundException;
import com.innowise.swimdom.mapper.BookingMapper;
import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.openapi.model.BookingFilterDTO;
import com.innowise.swimdom.openapi.model.BookingResponseDTO;
import com.innowise.swimdom.openapi.model.BookingUpdateRequestDTO;
import com.innowise.swimdom.repository.BookingRepository;
import com.innowise.swimdom.repository.PoolRepository;
import com.innowise.swimdom.repository.PoolWorkingHoursRepository;
import com.innowise.swimdom.repository.ScheduleRepository;
import com.innowise.swimdom.repository.UserRepository;
import com.innowise.swimdom.repository.UserSubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.innowise.swimdom.service.util.BookingTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PoolRepository poolRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private PoolWorkingHoursRepository poolWorkingHoursRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User testUser;
    private Pool testPool;
    private Schedule testSchedule;
    private UserSubscription testUserSubscription;
    private Booking testBooking;
    private BookingCreateRequestDTO testCreateRequest;
    private BookingUpdateRequestDTO testUpdateRequest;
    private BookingResponseDTO testResponse;

    @BeforeEach
    void setUp() {
        testUser = createTestUser();
        testPool = createTestPool();
        testSchedule = createTestSchedule();
        testUserSubscription = createTestUserSubscription();
        testBooking = createTestBooking();
        testCreateRequest = createTestBookingCreateRequestDTO();
        testUpdateRequest = createTestBookingUpdateRequestDTO();
        testResponse = createTestBookingResponseDTO();
    }

    @Test
    void createBooking_Success() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.of(testUserSubscription));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(testSchedule));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        when(bookingMapper.toBookingResponseDTO(testBooking)).thenReturn(testResponse);

        // WHEN
        BookingResponseDTO result = bookingService.createBooking(testCreateRequest);

        // THEN
        assertNotNull(result);
        assertEquals(testResponse, result);
        verify(bookingRepository).save(any(Booking.class));
        verify(bookingMapper).toBookingResponseDTO(testBooking);
    }

    @Test
    void createBooking_UserNotFound_ThrowsException() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(UserNotFoundException.class, () -> bookingService.createBooking(testCreateRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_UserSubscriptionNotFound_ThrowsException() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(UserSubscriptionNotFoundException.class, () -> bookingService.createBooking(testCreateRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_ScheduleNotFound_ThrowsException() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.of(testUserSubscription));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ScheduleNotFoundException.class, () -> bookingService.createBooking(testCreateRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void getBooking_Success() {
        // GIVEN
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(testBooking));
        when(bookingMapper.toBookingResponseDTO(testBooking)).thenReturn(testResponse);

        // WHEN
        BookingResponseDTO result = bookingService.getBooking(BOOKING_ID.toString());

        // THEN
        assertNotNull(result);
        assertEquals(testResponse, result);
        verify(bookingMapper).toBookingResponseDTO(testBooking);
    }

    @Test
    void getBooking_NotFound_ReturnsNull() {
        // GIVEN
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        // WHEN
        BookingResponseDTO result = bookingService.getBooking(BOOKING_ID.toString());

        // THEN
        assertNull(result);
        verify(bookingMapper, never()).toBookingResponseDTO(any());
    }

    @Test
    void getBooking_InvalidId_ThrowsException() {
        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> bookingService.getBooking("invalid-id"));
        verify(bookingRepository, never()).findById(any());
    }

    @Test
    void getBookingsByUser_Success() {
        // GIVEN
        List<Booking> bookings = List.of(testBooking);
        List<BookingResponseDTO> expectedResponses = List.of(testResponse);

        when(bookingRepository.findByUserId(USER_ID)).thenReturn(bookings);
        when(bookingMapper.toBookingResponseDTO(testBooking)).thenReturn(testResponse);

        // WHEN
        List<BookingResponseDTO> result = bookingService.getBookingsByUser(USER_ID);

        // THEN
        assertNotNull(result);
        assertEquals(expectedResponses, result);
        verify(bookingMapper).toBookingResponseDTO(testBooking);
    }

    @Test
    void getBookingsByUser_EmptyResult_ReturnsEmptyList() {
        // GIVEN
        when(bookingRepository.findByUserId(USER_ID)).thenReturn(List.of());

        // WHEN
        List<BookingResponseDTO> result = bookingService.getBookingsByUser(USER_ID);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingMapper, never()).toBookingResponseDTO(any());
    }

    @Test
    void getAllBookings_Success() {
        // GIVEN
        List<Booking> bookings = List.of(testBooking);
        List<BookingResponseDTO> expectedResponses = List.of(testResponse);

        when(bookingRepository.findAll()).thenReturn(bookings);
        when(bookingMapper.toBookingResponseDTO(testBooking)).thenReturn(testResponse);

        // WHEN
        List<BookingResponseDTO> result = bookingService.getAllBookings();

        // THEN
        assertNotNull(result);
        assertEquals(expectedResponses, result);
        verify(bookingMapper).toBookingResponseDTO(testBooking);
    }

    @Test
    void getAllBookings_EmptyResult_ReturnsEmptyList() {
        // GIVEN
        when(bookingRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<BookingResponseDTO> result = bookingService.getAllBookings();

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingMapper, never()).toBookingResponseDTO(any());
    }

    @Test
    void deleteBooking_Success() {
        // GIVEN
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(testBooking));

        // WHEN
        bookingService.deleteBooking(BOOKING_ID);

        // THEN
        verify(bookingRepository).delete(testBooking);
    }

    @Test
    void deleteBooking_NotFound_ThrowsException() {
        // GIVEN
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(BookingNotFoundException.class, () -> bookingService.deleteBooking(BOOKING_ID));
        verify(bookingRepository, never()).delete(any(Booking.class));
    }

    @Test
    void updateBooking_Success() {
        // GIVEN
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(testSchedule));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        when(bookingMapper.toBookingResponseDTO(testBooking)).thenReturn(testResponse);

        // WHEN
        BookingResponseDTO result = bookingService.updateBooking(testUpdateRequest);

        // THEN
        assertNotNull(result);
        assertEquals(testResponse, result);
        verify(bookingRepository).save(any(Booking.class));
        verify(bookingMapper).toBookingResponseDTO(testBooking);
    }

    @Test
    void updateBooking_BookingNotFound_ThrowsException() {
        // GIVEN
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(BookingNotFoundException.class, () -> bookingService.updateBooking(testUpdateRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateBooking_UserNotFound_ThrowsException() {
        // GIVEN
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(BookingNotFoundException.class, () -> bookingService.updateBooking(testUpdateRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void updateBooking_ScheduleNotFound_ThrowsException() {
        // GIVEN
        when(bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(BookingNotFoundException.class, () -> bookingService.updateBooking(testUpdateRequest));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void getBookingsByFilter_Success() {
        // GIVEN
        BookingFilterDTO filterDTO = new BookingFilterDTO();
        filterDTO.setUserId(USER_ID);
        filterDTO.setStatus(BookingFilterDTO.StatusEnum.CONFIRMED);
        
        List<Booking> bookings = List.of(testBooking);
        List<BookingResponseDTO> expectedResponses = List.of(testResponse);

        when(bookingRepository.findAll(any(Specification.class))).thenReturn(bookings);
        when(bookingMapper.toBookingResponseDTO(testBooking)).thenReturn(testResponse);

        // WHEN
        List<BookingResponseDTO> result = bookingService.getBookingsByFilter(filterDTO);

        // THEN
        assertNotNull(result);
        assertEquals(expectedResponses, result);
        verify(bookingRepository).findAll(any(Specification.class));
        verify(bookingMapper).toBookingResponseDTO(testBooking);
    }

    @Test
    void getBookingsByFilter_EmptyResult_ReturnsEmptyList() {
        // GIVEN
        BookingFilterDTO filterDTO = new BookingFilterDTO();
        filterDTO.setUserId(USER_ID);

        when(bookingRepository.findAll(any(Specification.class))).thenReturn(List.of());

        // WHEN
        List<BookingResponseDTO> result = bookingService.getBookingsByFilter(filterDTO);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository).findAll(any(Specification.class));
        verify(bookingMapper, never()).toBookingResponseDTO(any());
    }

    @Test
    void isAvailable_ReturnsTrue_WhenNoConflicts() {
        // GIVEN
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        when(bookingRepository.findBookingByTime(POOL_ID, startTime.minusMinutes(1), endTime.plusMinutes(1)))
            .thenReturn(false);

        // WHEN
        boolean result = bookingService.isAvailable(POOL_ID, startTime, endTime);

        // THEN
        assertTrue(result);
        verify(bookingRepository).findBookingByTime(POOL_ID, startTime.minusMinutes(1), endTime.plusMinutes(1));
    }

    @Test
    void isAvailable_ReturnsFalse_WhenConflictsExist() {
        // GIVEN
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        when(bookingRepository.findBookingByTime(POOL_ID, startTime.minusMinutes(1), endTime.plusMinutes(1)))
            .thenReturn(true);

        // WHEN
        boolean result = bookingService.isAvailable(POOL_ID, startTime, endTime);

        // THEN
        assertFalse(result);
        verify(bookingRepository).findBookingByTime(POOL_ID, startTime.minusMinutes(1), endTime.plusMinutes(1));
    }
} 