package com.innowise.swimdom.service;

import com.innowise.swimdom.entity.Booking;
import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.enums.SubscriptionDuration;
import com.innowise.swimdom.exception.BookingConflictException;
import com.innowise.swimdom.exception.BookingNotFoundException;
import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.openapi.model.BookingResponseDTO;
import com.innowise.swimdom.repository.BookingRepository;
import com.innowise.swimdom.repository.PoolRepository;
import com.innowise.swimdom.repository.PoolWorkingHoursRepository;
import com.innowise.swimdom.repository.ScheduleRepository;
import com.innowise.swimdom.repository.SubscriptionRepository;
import com.innowise.swimdom.repository.UserRepository;
import com.innowise.swimdom.repository.UserSubscriptionRepository;
import com.innowise.swimdom.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class BookingServiceIntTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PoolRepository poolRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private PoolWorkingHoursRepository poolWorkingHoursRepository;

    private User testUser;
    private Pool testPool;
    private Schedule testSchedule;
    private UserSubscription testUserSubscription;
    private PoolWorkingHours testWorkingHours;
    private BookingCreateRequestDTO testCreateRequest;
    private Subscription tstSubscription;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        // Clean up test data
        bookingRepository.deleteAll();
        userSubscriptionRepository.deleteAll();
        scheduleRepository.deleteAll();
        poolWorkingHoursRepository.deleteAll();
        poolRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User();

        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setName("Name");
        testUser.setSurname("TestSurname");
        testUser.setPhone("3751231236343");
        testUser = userRepository.save(testUser);

        // Create test pool
        testPool = new Pool();
        testPool.setName("Test Pool");
        testPool.setDescription("Test pool description");
        testPool.setLocation("Test location");
        testPool.setCapacity(10);
        testPool = poolRepository.save(testPool);

        // Create test working hours
        testWorkingHours = new PoolWorkingHours();
        testWorkingHours.setPool(testPool);
        testWorkingHours.setWeekday((short) 1); // Monday
        testWorkingHours.setOpenTime(LocalTime.of(8, 0));
        testWorkingHours.setCloseTime(LocalTime.of(22, 0));
        testWorkingHours = poolWorkingHoursRepository.save(testWorkingHours);

        // Найдем ближайший понедельник (или текущий, если сегодня понедельник)
        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.MONDAY));

        // Создаем расписание на ближайший понедельник с 17:00 до 18:00
        testSchedule = new Schedule();
        testSchedule.setPool(testPool);
        testSchedule.setStartDatetime(LocalDateTime.of(nextMonday, LocalTime.of(17, 0)));
        testSchedule.setEndDatetime(LocalDateTime.of(nextMonday, LocalTime.of(18, 0)));
        testSchedule = scheduleRepository.save(testSchedule);

        // Create test subscription
        tstSubscription = new Subscription();
        tstSubscription.setName("Name");
        tstSubscription.setDescription("Name");
        tstSubscription.setPrice(BigDecimal.valueOf(8f));
        tstSubscription.setDuration(SubscriptionDuration.MONTH);
        tstSubscription = subscriptionRepository.save(tstSubscription);

        // Create test user subscription с датами относительно сегодня
        testUserSubscription = new UserSubscription();
        testUserSubscription.setSubscription(tstSubscription);
        testUserSubscription.setUser(testUser);
        testUserSubscription.setStartDate(today);
        testUserSubscription.setEndDate(today.plusMonths(1));
        testUserSubscription.setEstimate(8);
        testUserSubscription = userSubscriptionRepository.save(testUserSubscription);

        // Create test booking request с датой совпадающей с расписанием
        testCreateRequest = new BookingCreateRequestDTO();
        testCreateRequest.setUserId(testUser.getId());
        testCreateRequest.setScheduleId(testSchedule.getId());
        testCreateRequest.setUserSubscriptionId(testUserSubscription.getId());
        testCreateRequest.setBookingDatetime(LocalDateTime.of(nextMonday, LocalTime.of(17, 0)));
    }

    @Test
    void createBooking_Integration_Success() {
        // WHEN
        BookingResponseDTO result = bookingService.createBooking(testCreateRequest);

        // THEN
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(testSchedule.getId(), result.getScheduleId());
        assertEquals(testUserSubscription.getId(), result.getUserSubscriptionId());
        assertEquals(BookingResponseDTO.StatusEnum.CONFIRMED, result.getStatus());
        assertFalse(result.getNotificationSent());

        // Verify booking was saved to database
        List<Booking> savedBookings = bookingRepository.findAll();
        assertEquals(1, savedBookings.size());
        assertEquals(testUser.getId(), savedBookings.get(0).getUser().getId());
    }

    @Test
    void createBooking_Integration_DuplicateBooking_ThrowsException() {
        // GIVEN - Create first booking
        bookingService.createBooking(testCreateRequest);

        // WHEN & THEN - Try to create duplicate booking
        assertThrows(BookingConflictException.class, () -> bookingService.createBooking(testCreateRequest));

        // Verify only one booking exists
        List<Booking> savedBookings = bookingRepository.findAll();
        assertEquals(1, savedBookings.size());
    }

    @Test
    void getBooking_Integration_Success() {
        // GIVEN - Create a booking
        BookingResponseDTO createdBooking = bookingService.createBooking(testCreateRequest);

        // WHEN
        BookingResponseDTO result = bookingService.getBooking(createdBooking.getId().toString());

        // THEN
        assertNotNull(result);
        assertEquals(createdBooking.getId(), result.getId());
        assertEquals(testUser.getId(), result.getUserId());
    }

    @Test
    void getBooking_Integration_NotFound_ReturnsNull() {
        // WHEN
        BookingResponseDTO result = bookingService.getBooking(UUID.randomUUID().toString());

        // THEN
        assertNull(result);
    }

    @Test
    void getBookingsByUser_Integration_Success() {
        // GIVEN - Create a booking
        bookingService.createBooking(testCreateRequest);

        // WHEN
        List<BookingResponseDTO> result = bookingService.getBookingsByUser(testUser.getId());

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getUserId());
    }

    @Test
    void deleteBooking_Integration_Success() {
        // GIVEN - Create a booking
        BookingResponseDTO createdBooking = bookingService.createBooking(testCreateRequest);

        // WHEN
        bookingService.deleteBooking(createdBooking.getId());

        // THEN
        List<Booking> savedBookings = bookingRepository.findAll();
        assertEquals(0, savedBookings.size());
    }

    @Test
    void deleteBooking_Integration_NotFound_ThrowsException() {
        // WHEN & THEN
        assertThrows(BookingNotFoundException.class, () -> bookingService.deleteBooking(UUID.randomUUID()));
    }

    @Test
    void isAvailable_Integration_AfterBooking_ReturnsFalse() {
        // GIVEN - Create a booking
        bookingService.createBooking(testCreateRequest);

        LocalTime startTime = LocalTime.now();
        LocalTime endTime = LocalTime.now().plusHours(3);

        // WHEN
        boolean result = bookingService.isAvailable(testPool.getId(), startTime, endTime);

        // THEN
        assertFalse(result);
    }
} 