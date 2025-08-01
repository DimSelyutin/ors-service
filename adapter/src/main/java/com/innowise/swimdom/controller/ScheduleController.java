package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.model.ScheduleDto;
import com.innowise.swimdom.service.ScheduleService;
import lombok.RequiredArgsConstructor;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller for managing schedules.
 */
@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Creates a new schedule.
     *
     * @param scheduleDto the schedule data
     * @return the created schedule
     */
    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody ScheduleDto scheduleDto) {
        ScheduleDto createdSchedule = scheduleService.createSchedule(scheduleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
    }

    /**
     * Retrieves a schedule by ID.
     *
     * @param id the schedule ID
     * @return the schedule or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDto> getSchedule(@PathVariable String id) {
        ScheduleDto schedule = scheduleService.getSchedule(id);
        if (schedule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schedule);
    }

    /**
     * Retrieves all schedules.
     *
     * @return list of all schedules
     */
    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getAllSchedules() {
        List<ScheduleDto> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    /**
     * Retrieves all schedules for a specific pool.
     *
     * @param poolId the pool ID
     * @return list of schedules for the pool
     */
    @GetMapping("/by-pool/{poolId}")
    public ResponseEntity<List<ScheduleDto>> getSchedulesByPool(@PathVariable UUID poolId) {
        List<ScheduleDto> schedules = scheduleService.getSchedulesByPool(poolId);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Deletes a schedule by ID.
     *
     * @param id the schedule ID
     * @return 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves schedules within a time range.
     *
     * @param from start of the time range
     * @param to   end of the time range
     * @return list of schedules in the time range
     */
    @GetMapping("/in-range")
    public ResponseEntity<List<ScheduleDto>> getSchedulesInRange(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        List<ScheduleDto> schedules = scheduleService.getSchedulesInRange(from, to);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Retrieves schedules for a pool within a time range.
     *
     * @param poolId the pool ID
     * @param from   start of the time range
     * @param to     end of the time range
     * @return list of schedules for the pool in the time range
     */
    @GetMapping("/by-pool/{poolId}/in-range")
    public ResponseEntity<List<ScheduleDto>> getSchedulesByPoolInRange(
            @PathVariable UUID poolId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        List<ScheduleDto> schedules = scheduleService.getSchedulesByPoolInRange(poolId, from, to);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Updates a schedule.
     *
     * @param id          the schedule ID
     * @param scheduleDto the updated schedule data
     * @return the updated schedule
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDto> updateSchedule(
            @PathVariable UUID id,
            @RequestBody ScheduleDto scheduleDto) {
        scheduleDto.setId(id);
        ScheduleDto updatedSchedule = scheduleService.updateSchedule(scheduleDto);
        return ResponseEntity.ok(updatedSchedule);
    }

    /**
     * Checks if a time slot is available for scheduling.
     *
     * @param poolId    the pool ID
     * @param startTime the start time
     * @param endTime   the end time
     * @return true if available, false otherwise
     */
    @GetMapping("/availability")
    public ResponseEntity<Boolean> isTimeSlotAvailable(
            @RequestParam UUID poolId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        boolean isAvailable = scheduleService.isTimeSlotAvailable(poolId, startTime, endTime);
        return ResponseEntity.ok(isAvailable);
    }
}
