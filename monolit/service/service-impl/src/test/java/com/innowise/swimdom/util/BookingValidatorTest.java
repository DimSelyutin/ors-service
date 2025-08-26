package com.innowise.swimdom.util;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.repository.BookingRepository;
import com.innowise.swimdom.repository.PoolWorkingHoursRepository;
import com.innowise.swimdom.repository.ScheduleRepository;
import com.innowise.swimdom.repository.UserRepository;
import com.innowise.swimdom.repository.UserSubscriptionRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static com.innowise.swimdom.service.util.BookingTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingValidatorTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private PoolWorkingHoursRepository poolWorkingHoursRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderContext;

    @InjectMocks
    private BookingValidator bookingValidator;

    private BookingCreateRequestDTO validBookingDTO;
    private User testUser;
    private UserSubscription testUserSubscription;
    private Schedule testSchedule;
    private Pool testPool;

    @BeforeEach
    void setUp() {
        validBookingDTO = createTestBookingCreateRequestDTO();
        testUser = createTestUser();
        testUserSubscription = createTestUserSubscription();
        testSchedule = createTestSchedule();
        testPool = createTestPool();

        // Setup context mocks
        doNothing().when(context).disableDefaultConstraintViolation();
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(any())).thenReturn(nodeBuilderContext);
        when(nodeBuilderContext.addConstraintViolation()).thenReturn(context);
    }



    @Test
    void isValid_UserNotFound_ReturnsFalse() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // WHEN
        boolean result = bookingValidator.isValid(validBookingDTO, context);

        // THEN
        assertFalse(result);
        verify(context, times(1)).buildConstraintViolationWithTemplate(any());
    }

    @Test
    void isValid_UserSubscriptionNotFound_ReturnsFalse() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.empty());

        // WHEN
        boolean result = bookingValidator.isValid(validBookingDTO, context);

        // THEN
        assertFalse(result);
        verify(context, times(1)).buildConstraintViolationWithTemplate(any());
    }

    @Test
    void isValid_ScheduleNotFound_ReturnsFalse() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.of(testUserSubscription));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.empty());

        // WHEN
        boolean result = bookingValidator.isValid(validBookingDTO, context);

        // THEN
        assertFalse(result);
        verify(context, times(1)).buildConstraintViolationWithTemplate(any());
    }

    @Test
    void isValid_BookingAlreadyExists_ReturnsFalse() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.of(testUserSubscription));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(testSchedule));
        when(bookingRepository.findByUserIdAndScheduleId(USER_ID, SCHEDULE_ID)).thenReturn(Optional.of(createTestBooking()));

        // WHEN
        boolean result = bookingValidator.isValid(validBookingDTO, context);

        // THEN
        assertFalse(result);
        verify(context, times(1)).buildConstraintViolationWithTemplate(any());
    }

    @Test
    void isValid_BookingTimeOutsideWorkingHours_ReturnsFalse() {
        // GIVEN
        PoolWorkingHours workingHours = new PoolWorkingHours();
        workingHours.setOpenTime(LocalTime.of(8, 0));
        workingHours.setCloseTime(LocalTime.of(22, 0));
        workingHours.setWeekday((short) 5); // Пятница (21 июня 2024)

        Schedule scheduleOutsideHours = createTestSchedule();
        scheduleOutsideHours.setStartDatetime(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0)));
        scheduleOutsideHours.setEndDatetime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0)));

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.of(testUserSubscription));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleOutsideHours));
        when(bookingRepository.findByUserIdAndScheduleId(USER_ID, SCHEDULE_ID)).thenReturn(Optional.empty());
        when(poolWorkingHoursRepository.findByPoolIdAndWeekday(any(), anyShort())).thenReturn(Optional.of(workingHours));

        // WHEN
        boolean result = bookingValidator.isValid(validBookingDTO, context);

        // THEN
        assertFalse(result);
        verify(context, times(1)).buildConstraintViolationWithTemplate(any());
    }

    @Test
    void isValid_UserSubscriptionDoesNotBelongToUser_ReturnsFalse() {
        // GIVEN
        User differentUser = createTestUser();
        differentUser.setId(UUID.randomUUID());
        testUserSubscription.setUser(differentUser);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.of(testUserSubscription));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(testSchedule));
        when(bookingRepository.findByUserIdAndScheduleId(USER_ID, SCHEDULE_ID)).thenReturn(Optional.empty());
        when(poolWorkingHoursRepository.findByPoolIdAndWeekday(any(), anyShort())).thenReturn(Optional.empty());

        // WHEN
        boolean result = bookingValidator.isValid(validBookingDTO, context);

        // THEN
        assertFalse(result);
        verify(context, times(1)).buildConstraintViolationWithTemplate(any());
    }

    @Test
    void isValid_UserSubscriptionExpired_ReturnsFalse() {
        // GIVEN
        testUserSubscription.setEndDate(LocalDate.now().minusDays(1));

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userSubscriptionRepository.findById(USER_SUBSCRIPTION_ID)).thenReturn(Optional.of(testUserSubscription));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(testSchedule));
        when(bookingRepository.findByUserIdAndScheduleId(USER_ID, SCHEDULE_ID)).thenReturn(Optional.empty());
        when(poolWorkingHoursRepository.findByPoolIdAndWeekday(any(), anyShort())).thenReturn(Optional.empty());

        // WHEN
        boolean result = bookingValidator.isValid(validBookingDTO, context);

        // THEN
        assertFalse(result);
        verify(context, times(1)).buildConstraintViolationWithTemplate(any());
    }

}
