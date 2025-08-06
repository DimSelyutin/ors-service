package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.model.BookingCreateRequestDTO;
import com.innowise.swimdom.openapi.model.BookingFilterDTO;
import com.innowise.swimdom.openapi.model.BookingResponseDTO;
import com.innowise.swimdom.openapi.model.BookingUpdateRequestDTO;
import com.innowise.swimdom.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingResponseDTO testBookingResponse;
    private BookingCreateRequestDTO testBookingCreateRequest;
    private BookingUpdateRequestDTO testBookingUpdateRequest;
    private BookingFilterDTO testBookingFilter;
    private List<BookingResponseDTO> testBookingList;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        UUID scheduleId = UUID.randomUUID();
        UUID userSubscriptionId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();

        testBookingResponse = new BookingResponseDTO();
        testBookingResponse.setId(bookingId);
        testBookingResponse.setUserId(userId);
        testBookingResponse.setScheduleId(scheduleId);
        testBookingResponse.setUserSubscriptionId(userSubscriptionId);
        testBookingResponse.setBookingDatetime(LocalDateTime.now());

        testBookingCreateRequest = new BookingCreateRequestDTO();
        testBookingCreateRequest.setUserId(userId);
        testBookingCreateRequest.setScheduleId(scheduleId);
        testBookingCreateRequest.setUserSubscriptionId(userSubscriptionId);
        testBookingCreateRequest.setBookingDatetime(LocalDateTime.now());

        testBookingUpdateRequest = new BookingUpdateRequestDTO();
        testBookingUpdateRequest.setId(bookingId);
        testBookingUpdateRequest.setBookingDatetime(LocalDateTime.now().plusHours(1));

        testBookingFilter = new BookingFilterDTO();
        testBookingFilter.setUserId(userId);

        testBookingList = Arrays.asList(testBookingResponse);
    }

    @Test
    void createBooking_Success() throws Exception {
        when(bookingService.createBooking(any(BookingCreateRequestDTO.class))).thenReturn(testBookingResponse);

        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookingCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(testBookingResponse)));
    }

    @Test
    void createBooking_InvalidJson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingsById_Success() throws Exception {
        UUID bookingId = UUID.randomUUID();
        when(bookingService.getBooking(eq(bookingId.toString()))).thenReturn(testBookingResponse);

        mockMvc.perform(get("/api/v1/bookings/{id}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testBookingResponse)));
    }

    @Test
    void getBookingsById_NotFound_ReturnsNotFound() throws Exception {
        UUID bookingId = UUID.randomUUID();
        when(bookingService.getBooking(eq(bookingId.toString()))).thenReturn(null);

        mockMvc.perform(get("/api/v1/bookings/{id}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // @Test
    // void updateBookingsById_Success() throws Exception {
    //     UUID bookingId = UUID.randomUUID();
    //     testBookingUpdateRequest.setId(bookingId);
    //     testBookingUpdateRequest.setBookingDatetime(LocalDateTime.now().plusMonths(4));
    //     when(bookingService.updateBooking(any(BookingUpdateRequestDTO.class))).thenReturn(testBookingResponse);
    //
    //     mockMvc.perform(put("/api/v1/bookings/{id}", bookingId)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(testBookingUpdateRequest)))
    //             .andExpect(status().isOk())
    //             .andExpect(content().json(objectMapper.writeValueAsString(testBookingResponse)));
    // }

    @Test
    void updateBookingsById_InvalidJson_ReturnsBadRequest() throws Exception {
        UUID bookingId = UUID.randomUUID();

        mockMvc.perform(put("/api/v1/bookings/{id}", bookingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBookingsById_Success() throws Exception {
        UUID bookingId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/bookings/{id}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getBookingsByUser_Success() throws Exception {
        UUID userId = UUID.randomUUID();
        when(bookingService.getBookingsByUser(eq(userId))).thenReturn(testBookingList);

        mockMvc.perform(get("/api/v1/bookings/user")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testBookingList)));
    }

    @Test
    void getBookingsByUser_EmptyList_Success() throws Exception {
        UUID userId = UUID.randomUUID();
        when(bookingService.getBookingsByUser(eq(userId))).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/bookings/user")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBookingsByRange_Success() throws Exception {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusDays(1);
        when(bookingService.getBookingsInRange(eq(from), eq(to))).thenReturn(testBookingList);

        mockMvc.perform(get("/api/v1/bookings")
                .param("from", from.toString())
                .param("to", to.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testBookingList)));
    }

    @Test
    void getBookingsByRange_EmptyList_Success() throws Exception {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusDays(1);
        when(bookingService.getBookingsInRange(eq(from), eq(to))).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/bookings")
                .param("from", from.toString())
                .param("to", to.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBookingsByFilters_Success() throws Exception {
        when(bookingService.getBookingsByFilter(any(BookingFilterDTO.class))).thenReturn(testBookingList);

        mockMvc.perform(post("/api/v1/bookings/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookingFilter)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testBookingList)));
    }

    @Test
    void getBookingsByFilters_EmptyList_Success() throws Exception {
        when(bookingService.getBookingsByFilter(any(BookingFilterDTO.class))).thenReturn(Arrays.asList());

        mockMvc.perform(post("/api/v1/bookings/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookingFilter)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBookingsByFilters_InvalidJson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/bookings/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }
} 