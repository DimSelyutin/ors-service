package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Booking;
import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.entity.PoolWorkingHours;
import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.enums.BookingStatus;
import com.innowise.swimdom.exception.BookingConflictException;
import com.innowise.swimdom.exception.BookingNotFoundException;
import com.innowise.swimdom.exception.InvalidTimeSlotException;
import com.innowise.swimdom.exception.ScheduleNotFoundException;
import com.innowise.swimdom.exception.UserNotFoundException;
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
import com.innowise.swimdom.repository.specification.BookingSpecification;
import com.innowise.swimdom.service.BookingService;
import com.innowise.swimdom.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import static com.innowise.swimdom.util.Constants.BOOKING_ALREADY_EXIST;
import static com.innowise.swimdom.util.Constants.BOOKING_DATE_OUTSIDE;
import static com.innowise.swimdom.util.Constants.BOOKING_TIME_OUTSIDE;
import static com.innowise.swimdom.util.Constants.SCHEDULE_NOT_FOUND;
import static com.innowise.swimdom.util.Constants.USER_SUBSCRIPTION_EXPIRED;
import static com.innowise.swimdom.util.Constants.USER_SUBSCRIPTION_NOT_BELONG;
import static com.innowise.swimdom.util.Constants.USER_SUBSCRIPTION_NOT_FOUND;

/**
 * Implementation for BookingService.
 */
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PoolRepository poolRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PoolWorkingHoursRepository poolWorkingHoursRepository;
    private final BookingMapper bookingMapper;

    /**
     * Creates a new booking for the pool at the specified time slot.
     * Checks availability, user rights and subscriptions.
     * Sends email notification after successful booking.
     */
    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingCreateRequestDTO bookingDTO) {
        User user = userRepository.findById(bookingDTO.getUserId())
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND + bookingDTO.getUserId()));

        Schedule schedule = scheduleRepository.findById(bookingDTO.getScheduleId())
            .orElseThrow(
                () -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND + bookingDTO.getScheduleId()));

        Pool pool = schedule.getPool();

        Optional<Booking> activeBooking = bookingRepository.findByUserIdAndScheduleId(
            user.getId(), schedule.getId());

        if (activeBooking.isPresent()) {
            throw new BookingConflictException(
                BOOKING_ALREADY_EXIST);
        }

        // Check if booking time is within pool operating hours
        if (isWorkingHours(pool, schedule.getStartDatetime(), schedule.getEndDatetime())) {
            throw new InvalidTimeSlotException(BOOKING_TIME_OUTSIDE);
        }

        // Check subscription
        UserSubscription userSubscription = userSubscriptionRepository.findById(bookingDTO.getUserSubscriptionId())
            .orElseThrow(() -> new BookingNotFoundException(
                USER_SUBSCRIPTION_NOT_FOUND + bookingDTO.getUserSubscriptionId()));

        // Check that subscription belongs to user
        if (!userSubscription.getUser().getId().equals(user.getId())) {
            throw new BookingConflictException(USER_SUBSCRIPTION_NOT_BELONG);
        }

        // Check that subscription is active
        if (userSubscription.getEndDate().isBefore(LocalDate.now())) {
            throw new BookingConflictException(USER_SUBSCRIPTION_EXPIRED);
        }

        // Check that booking time is within subscription period
        LocalDate scheduleDate = schedule.getStartDatetime().toLocalDate();
        if (scheduleDate.isBefore(userSubscription.getStartDate())
            || scheduleDate.isAfter(userSubscription.getEndDate())) {
            throw new BookingConflictException(BOOKING_DATE_OUTSIDE);
        }

        Booking booking = Booking.builder()
            .user(user)
            .userSubscription(userSubscription)
            .schedule(schedule)
            .status(BookingStatus.CONFIRMED)
            .bookingDatetime(bookingDTO.getBookingDatetime())
            .notificationSent(false)
            .build();

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingResponseDTO(savedBooking);
    }

    /**
     * Retrieves a booking by its unique ID.
     */
    @Override
    public BookingResponseDTO getBooking(String bookingId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(bookingId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid booking ID format.");
        }
        return bookingRepository.findById(uuid)
            .map(bookingMapper::toBookingResponseDTO)
            .orElse(null);
    }

    /**
     * Retrieves all bookings for a given user.
     */
    @Override
    public List<BookingResponseDTO> getBookingsByUser(UUID userId) {
        return bookingRepository.findByUserId(userId).stream()
            .map(bookingMapper::toBookingResponseDTO)
            .toList();
    }

    /**
     * Retrieves all bookings.
     */
    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(bookingMapper::toBookingResponseDTO)
            .toList();
    }

    /**
     * Deletes a booking by its ID.
     */
    @Override
    public void deleteBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException(Constants.BOOKING_NOT_FOUND + bookingId));
        bookingRepository.delete(booking);
    }

    /**
     * Retrieves bookings based on filter criteria.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByFilter(BookingFilterDTO filterDTO) {
        Specification<Booking> specification = BookingSpecification.byFilter(filterDTO);
        return bookingRepository.findAll(specification).stream()
            .map(bookingMapper::toBookingResponseDTO)
            .toList();
    }

    /**
     * Overloaded version of isAvailable for specific pool.
     * Checks capacity and booking overlaps.
     */
    public boolean isAvailable(UUID poolId, LocalTime startTime, LocalTime endTime) {
        return bookingRepository.findByPoolIdAndStartDatetimeBetween(
            poolId, startTime.minusMinutes(1), endTime.plusMinutes(1));
    }

    /**
     * Updates a booking (e.g., changes time).
     * Checks constraints, availability and user rights.
     */
    @Override
    @Transactional
    public BookingResponseDTO updateBooking(BookingUpdateRequestDTO bookingUpdateRequestDTO) {
        UUID bookingId = bookingUpdateRequestDTO.getId();
        bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        Booking existingBooking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException(
                "Booking not found with ID: " + bookingId));

        userRepository.findById(bookingUpdateRequestDTO.getUserId())
            .orElseThrow(() -> new BookingNotFoundException("User not found with ID: "
                + bookingUpdateRequestDTO.getUserId()));

        Schedule newSchedule = scheduleRepository.findById(bookingUpdateRequestDTO.getScheduleId())
            .orElseThrow(() -> new BookingNotFoundException("Schedule not found with ID: "
                + bookingUpdateRequestDTO.getScheduleId()));
        // Update booking fields
        existingBooking.setSchedule(newSchedule);
        existingBooking.setBookingDatetime(bookingUpdateRequestDTO.getBookingDatetime());
        existingBooking.setStatus(BookingStatus.valueOf(bookingUpdateRequestDTO.getStatus().toString()));
        existingBooking.setNotificationSent(bookingUpdateRequestDTO.getNotificationSent());

        Booking updatedBooking = bookingRepository.save(existingBooking);

        return bookingMapper.toBookingResponseDTO(updatedBooking);
    }

    /**
     * Checks if booking time is within pool operating hours.
     */
    private boolean isWorkingHours(Pool pool, LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime startTimeOfDay = startTime.toLocalTime();
        LocalTime endTimeOfDay = endTime.toLocalTime();

        int dayOfWeek = startTime.getDayOfWeek().getValue();

        // Find working hours for this day of week
        Optional<PoolWorkingHours> workingHours =
            poolWorkingHoursRepository.findByPoolIdAndWeekday(pool.getId(), (short) dayOfWeek);

        if (workingHours.isEmpty()) {
            return true;
        }

        PoolWorkingHours hours = workingHours.get();
        return startTimeOfDay.isBefore(hours.getOpenTime()) || endTimeOfDay.isAfter(hours.getCloseTime());
    }
}
