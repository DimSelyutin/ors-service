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
                () -> new BookingNotFoundException("Schedule not found with ID: " + bookingDTO.getScheduleId()));

        Pool pool = schedule.getPool();

        Optional<Booking> activeBooking = bookingRepository.findByUserIdAndScheduleId(
            user.getId(), schedule.getId());

        if (activeBooking.isPresent()) {
            throw new BookingConflictException(
                "User already has a booking for this schedule. Please choose a different time slot.");
        }

        // Check availability and pool capacity
        if (!isAvailable(pool.getId(), schedule.getStartDatetime(), schedule.getEndDatetime())) {
            throw new BookingConflictException("The selected time slot is not available or pool is at full capacity.");
        }

        // Check if booking time is within pool operating hours
        if (isWorkingHours(pool, schedule.getStartDatetime(), schedule.getEndDatetime())) {
            throw new InvalidTimeSlotException("Booking time is outside of pool's operating hours.");
        }

        // Check subscription
        UserSubscription userSubscription = userSubscriptionRepository.findById(bookingDTO.getUserSubscriptionId())
            .orElseThrow(() -> new BookingNotFoundException(
                "User subscription not found with ID: " + bookingDTO.getUserSubscriptionId()));

        // Check that subscription belongs to user
        if (!userSubscription.getUser().getId().equals(user.getId())) {
            throw new BookingConflictException("User subscription does not belong to the user.");
        }

        // Check that subscription is active
        if (userSubscription.getEndDate().isBefore(LocalDate.now())) {
            throw new BookingConflictException("User subscription has expired.");
        }

        // Check that booking time is within subscription period
        LocalDate scheduleDate = schedule.getStartDatetime().toLocalDate();
        if (scheduleDate.isBefore(userSubscription.getStartDate())
            || scheduleDate.isAfter(userSubscription.getEndDate())) {
            throw new BookingConflictException("Booking date is outside of subscription period.");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setUserSubscription(userSubscription);
        booking.setSchedule(schedule);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookingDatetime(bookingDTO.getBookingDatetime());
        booking.setNotificationSent(false);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingResponseDTO(savedBooking);
    }

    /**
     * Retrieves a booking by its unique ID.
     */
    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUser(UUID userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new BookingNotFoundException("User not found with ID: " + userId));

        return bookingRepository.findByUserId(userId).stream()
            .map(bookingMapper::toBookingResponseDTO)
            .toList();
    }

    /**
     * Retrieves all bookings.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(bookingMapper::toBookingResponseDTO)
            .toList();
    }

    /**
     * Deletes a booking by its ID.
     */
    @Override
    @Transactional
    public void deleteBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException(Constants.BOOKING_NOT_FOUND + bookingId));
        bookingRepository.delete(booking);
    }

    /**
     * Retrieves all bookings within a specified time range.
     * Useful for admins to see load.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsInRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From and To dates cannot be null.");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        return bookingRepository.findByScheduleStartDatetimeBetween(from, to).stream()
            .map(bookingMapper::toBookingResponseDTO)
            .toList();
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
     * Checks if the pool is available for booking in the given time slot.
     * Takes into account pool capacity.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        List<Pool> allPools = poolRepository.findAll();
        for (Pool pool : allPools) {
            if (isAvailable(pool.getId(), startTime, endTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Overloaded version of isAvailable for specific pool.
     * Checks capacity and booking overlaps.
     */
    public boolean isAvailable(UUID poolId, LocalDateTime startTime, LocalDateTime endTime) {
        Optional<Pool> optionalPool = poolRepository.findById(poolId);
        if (optionalPool.isEmpty()) {
            return false;
        }
        Pool pool = optionalPool.get();

        // Check if requested slot is within pool operating hours
        if (isWorkingHours(pool, startTime, endTime)) {
            return false;
        }

        // Find all schedules for this pool in specified time range
        List<Schedule> overlappingSchedules = scheduleRepository.findByPoolIdAndStartDatetimeBetween(
            poolId, startTime.minusMinutes(1), endTime.plusMinutes(1));

        // Check how many bookings already exist for these schedules
        int totalBookings = 0;
        for (Schedule schedule : overlappingSchedules) {
            List<Booking> bookings = bookingRepository.findByScheduleId(schedule.getId());
            totalBookings += bookings.size();
        }

        return totalBookings < pool.getCapacity();
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

        // Check availability of new schedule
        if (!isAvailable(newSchedule.getPool().getId(), newSchedule.getStartDatetime(),
            newSchedule.getEndDatetime())) {
            throw new BookingConflictException("The new schedule is not available or pool is at full capacity.");
        }

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
