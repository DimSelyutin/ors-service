package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.openapi.model.BookingFilterDTO;
import com.innowise.swimdom.openapi.model.BookingResponseDTO;
import com.innowise.swimdom.openapi.model.BookingUpdateRequestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Booking management service for pool schedules.
 * Provides methods to create, retrieve, update, and delete bookings.
 */
@Service
public interface BookingService {

    /**
     * Creates a new booking for the pool at the specified time slot.
     *
     * @param bookingDTO the booking data transfer object containing booking details
     * @return the created booking details
     */
    BookingResponseDTO createBooking(BookingCreateRequestDTO bookingDTO);

    /**
     * Retrieves a booking by its unique ID.
     *
     * @param bookingId the ID of the booking
     * @return the booking details or null if not found
     */
    BookingResponseDTO getBooking(String bookingId);

    /**
     * Retrieves all bookings.
     *
     * @return list of all bookings
     */
    List<BookingResponseDTO> getAllBookings();

    /**
     * Retrieves all bookings for a given user.
     *
     * @param userId the ID of the user
     * @return list of bookings associated with the user
     */
    List<BookingResponseDTO> getBookingsByUser(UUID userId);

    /**
     * Deletes a booking by its ID.
     *
     * @param bookingId the ID of the booking to delete
     */
    void deleteBooking(UUID bookingId);

    /**
     * Retrieves all bookings within a specified time range.
     *
     * @param from start of the time range (inclusive)
     * @param to   end of the time range (exclusive)
     * @return list of bookings within the specified time range
     */
    List<BookingResponseDTO> getBookingsInRange(LocalDateTime from, LocalDateTime to);

    /**
     * Updates a booking (e.g., changes time).
     * Checks constraints, availability and user rights.
     */
    BookingResponseDTO updateBooking(BookingUpdateRequestDTO bookingUpdateRequestDTO);

    /**
     * Retrieves bookings by filter criteria.
     *
     * @param filterDTO the filter criteria
     * @return list of bookings matching the filter criteria
     */
    List<BookingResponseDTO> getBookingsByFilter(BookingFilterDTO filterDTO);
}
