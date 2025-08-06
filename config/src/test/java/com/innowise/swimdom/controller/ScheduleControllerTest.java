package com.innowise.swimdom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.swimdom.openapi.model.ScheduleDto;
import com.innowise.swimdom.service.ScheduleService;
import com.innowise.swimdom.service.impl.JwtTokenProvider;
import com.innowise.swimdom.util.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.innowise.swimdom.util.TestData.POOL_ID;
import static com.innowise.swimdom.util.TestData.SCHEDULE_END_DATETIME;
import static com.innowise.swimdom.util.TestData.SCHEDULE_ID;
import static com.innowise.swimdom.util.TestData.SCHEDULE_START_DATETIME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test

    void createSchedule_success() throws Exception {
        // GIVEN
        ScheduleDto scheduleDto = TestData.testScheduleDto;
        when(scheduleService.createSchedule(any(ScheduleDto.class))).thenReturn(scheduleDto);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/schedules")
                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "token")
                .content(objectMapper.writeValueAsString(scheduleDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(scheduleDto.getId().toString()))
            .andExpect(jsonPath("$.poolId").value(scheduleDto.getPoolId().toString()));

        verify(scheduleService, times(1)).createSchedule(any(ScheduleDto.class));
    }

    @Test
    void getSchedule_success() throws Exception {
        // GIVEN
        String scheduleId = SCHEDULE_ID.toString();
        ScheduleDto scheduleDto = TestData.testScheduleDto;
        when(scheduleService.getSchedule(scheduleId)).thenReturn(scheduleDto);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/schedules/{id}", scheduleId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(scheduleDto.getId().toString()))
            .andExpect(jsonPath("$.poolId").value(scheduleDto.getPoolId().toString()));

        verify(scheduleService, times(1)).getSchedule(scheduleId);
    }

    @Test
    void getSchedule_notFound() throws Exception {
        // GIVEN
        String scheduleId = SCHEDULE_ID.toString();
        when(scheduleService.getSchedule(scheduleId)).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/schedules/{id}", scheduleId))
            .andExpect(status().isNotFound());

        verify(scheduleService, times(1)).getSchedule(scheduleId);
    }

    @Test
    void getAllSchedules_success() throws Exception {
        // GIVEN
        List<ScheduleDto> schedules = List.of(TestData.testScheduleDto);
        when(scheduleService.getAllSchedules()).thenReturn(schedules);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/schedules"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(schedules.get(0).getId().toString()));

        verify(scheduleService, times(1)).getAllSchedules();
    }

    @Test
    void getSchedulesByPool_success() throws Exception {
        // GIVEN
        UUID poolId = POOL_ID;
        List<ScheduleDto> schedules = List.of(TestData.testScheduleDto);
        when(scheduleService.getSchedulesByPool(poolId)).thenReturn(schedules);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/schedules/by-pool/{poolId}", poolId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(schedules.get(0).getId().toString()));

        verify(scheduleService, times(1)).getSchedulesByPool(poolId);
    }

    @Test
    void deleteSchedule_success() throws Exception {
        // GIVEN
        UUID scheduleId = SCHEDULE_ID;
        doNothing().when(scheduleService).deleteSchedule(scheduleId);

        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/schedules/{id}", scheduleId))
            .andExpect(status().isNoContent());

        verify(scheduleService, times(1)).deleteSchedule(scheduleId);
    }

    @Test
    void getSchedulesInRange_success() throws Exception {
        // GIVEN
        LocalDateTime from = SCHEDULE_START_DATETIME.minusHours(1);
        LocalDateTime to = SCHEDULE_END_DATETIME.plusHours(1);
        List<ScheduleDto> schedules = List.of(TestData.testScheduleDto);
        when(scheduleService.getSchedulesInRange(from, to)).thenReturn(schedules);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/schedules/in-range")
                .param("from", from.toString())
                .param("to", to.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(schedules.get(0).getId().toString()));

        verify(scheduleService, times(1)).getSchedulesInRange(from, to);
    }

    @Test
    void getSchedulesByPoolInRange_success() throws Exception {
        // GIVEN
        UUID poolId = POOL_ID;
        LocalDateTime from = SCHEDULE_START_DATETIME.minusHours(1);
        LocalDateTime to = SCHEDULE_END_DATETIME.plusHours(1);
        List<ScheduleDto> schedules = List.of(TestData.testScheduleDto);
        when(scheduleService.getSchedulesByPoolInRange(poolId, from, to)).thenReturn(schedules);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/schedules/by-pool/{poolId}/in-range", poolId)
                .param("from", from.toString())
                .param("to", to.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(schedules.get(0).getId().toString()));

        verify(scheduleService, times(1)).getSchedulesByPoolInRange(poolId, from, to);
    }

    @Test
    void updateSchedule_success() throws Exception {
        // GIVEN
        UUID scheduleId = SCHEDULE_ID;
        ScheduleDto scheduleDto = TestData.testScheduleDto;
        when(scheduleService.updateSchedule(any(ScheduleDto.class))).thenReturn(scheduleDto);

        // WHEN & THEN
        mockMvc.perform(put("/api/v1/schedules/{id}", scheduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scheduleDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(scheduleDto.getId().toString()));

        verify(scheduleService, times(1)).updateSchedule(any(ScheduleDto.class));
    }

    @Test
    void isTimeSlotAvailable_success() throws Exception {
        // GIVEN
        UUID poolId = POOL_ID;
        LocalDateTime startTime = SCHEDULE_START_DATETIME;
        LocalDateTime endTime = SCHEDULE_END_DATETIME;
        when(scheduleService.isTimeSlotAvailable(poolId, startTime, endTime)).thenReturn(true);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/schedules/availability")
                .param("poolId", poolId.toString())
                .param("startTime", startTime.toString())
                .param("endTime", endTime.toString()))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        verify(scheduleService, times(1)).isTimeSlotAvailable(poolId, startTime, endTime);
    }
} 