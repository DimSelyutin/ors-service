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
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static com.innowise.swimdom.util.Constants.BOOKING_ALREADY_EXIST;
import static com.innowise.swimdom.util.Constants.BOOKING_DATE_OUTSIDE;
import static com.innowise.swimdom.util.Constants.BOOKING_TIME_OUTSIDE;
import static com.innowise.swimdom.util.Constants.SCHEDULE_NOT_FOUND;
import static com.innowise.swimdom.util.Constants.USER_NOT_FOUND;
import static com.innowise.swimdom.util.Constants.USER_SUBSCRIPTION_EXPIRED;
import static com.innowise.swimdom.util.Constants.USER_SUBSCRIPTION_NOT_BELONG;
import static com.innowise.swimdom.util.Constants.USER_SUBSCRIPTION_NOT_FOUND;

/**
 * Validator for booking.
 */
@Component
@RequiredArgsConstructor
public class BookingValidator implements ConstraintValidator<ValidateBooking, BookingCreateRequestDTO> {

    private final BookingRepository bookingRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final ScheduleRepository scheduleRepository;
    private final PoolWorkingHoursRepository poolWorkingHoursRepository;
    private final UserRepository userRepository;

    @Override
    public boolean isValid(BookingCreateRequestDTO bookingDTO, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();
        boolean isValid = true;
        Optional<User> userOptional = userRepository.findById(bookingDTO.getUserId());
        Optional<UserSubscription> userSubscriptionOptional =
            userSubscriptionRepository.findById(bookingDTO.getUserSubscriptionId());
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(bookingDTO.getScheduleId());

        if (userOptional.isEmpty()) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(USER_NOT_FOUND + bookingDTO.getUserId())
                .addPropertyNode("userId").addConstraintViolation();
        }
        if (userSubscriptionOptional.isEmpty()) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(
                    USER_SUBSCRIPTION_NOT_FOUND + bookingDTO.getUserSubscriptionId())
                .addPropertyNode("userSubscriptionId").addConstraintViolation();
        }
        if (scheduleOptional.isEmpty()) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(SCHEDULE_NOT_FOUND + bookingDTO.getScheduleId())
                .addPropertyNode("scheduleId").addConstraintViolation();
        }

        if (!isValid) {
            return false;
        }

        User user = userOptional.get();
        UserSubscription userSubscription = userSubscriptionOptional.get();
        Schedule schedule = scheduleOptional.get();
        Pool pool = schedule.getPool();

        if (bookingRepository.findByUserIdAndScheduleId(user.getId(), schedule.getId()).isPresent()) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(BOOKING_ALREADY_EXIST)
                .addPropertyNode("scheduleId").addConstraintViolation();
        }

        if (isWorkingHours(pool, schedule.getStartDatetime(), schedule.getEndDatetime())) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(BOOKING_TIME_OUTSIDE)
                .addPropertyNode("bookingDatetime").addConstraintViolation();
        }

        if (!userSubscription.getUser().getId().equals(user.getId())) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(
                    USER_SUBSCRIPTION_NOT_BELONG + user.getId())
                .addPropertyNode("userSubscriptionId").addConstraintViolation();
        }

        if (userSubscription.getEndDate().isBefore(LocalDate.now())) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(USER_SUBSCRIPTION_EXPIRED)
                .addPropertyNode("userSubscriptionId").addConstraintViolation();
        }

        LocalDate scheduleDate = schedule.getStartDatetime().toLocalDate();
        if (scheduleDate.isBefore(userSubscription.getStartDate())
            || scheduleDate.isAfter(userSubscription.getEndDate())) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(BOOKING_DATE_OUTSIDE)
                .addPropertyNode("bookingDatetime").addConstraintViolation();
        }

        return isValid;
    }

    /**
     * Checks if booking time is within pool operating hours.
     */
    private boolean isWorkingHours(Pool pool, LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime startTimeOfDay = startTime.toLocalTime();
        LocalTime endTimeOfDay = endTime.toLocalTime();

        int dayOfWeek = startTime.getDayOfWeek().getValue();

        Optional<PoolWorkingHours> workingHours =
            poolWorkingHoursRepository.findByPoolIdAndWeekday(pool.getId(), (short) dayOfWeek);

        if (workingHours.isEmpty()) {
            return true;
        }

        PoolWorkingHours hours = workingHours.get();
        return startTimeOfDay.isBefore(hours.getOpenTime()) || endTimeOfDay.isAfter(hours.getCloseTime());
    }
}
