package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.Booking;
import com.innowise.swimdom.util.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Booking entity.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {



    /**
     * Find bookings by user ID.
     *
     * @param userId the user ID
     * @return list of bookings for the user
     */
    List<Booking> findByUserId(UUID userId);

    /**
     * Find booking by user ID and schedule ID.
     *
     * @param userId     the user ID
     * @param scheduleId the schedule ID
     * @return optional booking
     */
    Optional<Booking> findByUserIdAndScheduleId(UUID userId, UUID scheduleId);

    /**
     * Find bookings by schedule ID.
     *
     * @param scheduleId the schedule ID
     * @return list of bookings for the schedule
     */
    List<Booking> findByScheduleId(UUID scheduleId);

    /**
     * Find bookings by schedule start datetime range.
     *
     * @param from start datetime (inclusive)
     * @param to   end datetime (exclusive)
     * @return list of bookings in the range
     */
    List<Booking> findByScheduleStartDatetimeBetween(LocalDateTime from, LocalDateTime to);

    /**
     * Find bookings by schedule start datetime range.
     *
     * @param openTime  start datetime (inclusive)
     * @param closeTime end datetime (exclusive)
     * @return list of bookings in the range
     */
    @Query(value = Constants.SQLQUERY, nativeQuery = true)
    boolean findBookingByTime(UUID id, LocalTime openTime, LocalTime closeTime);
}
