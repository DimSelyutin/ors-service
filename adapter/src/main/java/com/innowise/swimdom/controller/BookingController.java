package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.api.BookingsApi;
import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.openapi.model.BookingFilterDTO;
import com.innowise.swimdom.openapi.model.BookingResponseDTO;
import com.innowise.swimdom.openapi.model.BookingUpdateRequestDTO;
import com.innowise.swimdom.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller for bookings.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController implements BookingsApi {

    private final BookingService bookingService;

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable("id") UUID id) {
        bookingService.deleteBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingsById(@PathVariable("id") UUID id) {
        BookingResponseDTO booking = bookingService.getBooking(id.toString());
        if (booking != null) {
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBookingsById(@PathVariable("id") UUID id,
                                                                 @RequestBody
                                                                 BookingUpdateRequestDTO bookingUpdateRequestDTO) {
        BookingResponseDTO updatedBooking = bookingService.updateBooking(bookingUpdateRequestDTO);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    @PostMapping
    @Override
    public ResponseEntity<BookingResponseDTO> createBooking(
        @RequestBody BookingCreateRequestDTO bookingCreateRequestDTO) {
        BookingResponseDTO createdBooking = bookingService.createBooking(bookingCreateRequestDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/user")
    public ResponseEntity<List<BookingResponseDTO>> getBookingByUser(@RequestParam UUID userId) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUser(userId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @Override
    @PostMapping("/filter")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByFilters(
        @RequestBody BookingFilterDTO filterDTO) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByFilter(filterDTO);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
