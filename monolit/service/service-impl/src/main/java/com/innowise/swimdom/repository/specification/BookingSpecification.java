package com.innowise.swimdom.repository.specification;

import com.innowise.swimdom.entity.Booking;
import com.innowise.swimdom.enums.BookingStatus;
import com.innowise.swimdom.openapi.model.BookingFilterDTO;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Specification for filtering bookings.
 */
public class BookingSpecification {

    private static final String CREATED_AT = "createdAt";
    private static final String BOOK_DATE_TIME = "bookingDatetime";

    /**
     * Creates a specification based on the provided filter.
     *
     * @param filter the filter criteria
     * @return the specification
     */
    public static Specification<Booking> byFilter(BookingFilterDTO filter) {

        return Specification.where(byUserId(filter.getUserId()))
            .and(byUserSubscriptionId(filter.getUserSubscriptionId()))
            .and(byScheduleId(filter.getScheduleId()))
            .and(byStatus(filter.getStatus()))
            .and(byBookingDatetimeRange(filter.getBookingDatetimeFrom(), filter.getBookingDatetimeTo()))
            .and(byCreatedAtRange(filter.getCreatedAtFrom(), filter.getCreatedAtTo()))
            .and(byNotificationSent(filter.getNotificationSent()));
    }

    /**
     * Filter by user ID.
     */
    private static Specification<Booking> byUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    /**
     * Filter by user subscription ID.
     */
    private static Specification<Booking> byUserSubscriptionId(UUID userSubscriptionId) {
        return (root, query, criteriaBuilder) -> {
            if (userSubscriptionId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("userSubscription").get("id"), userSubscriptionId);
        };
    }

    /**
     * Filter by schedule ID.
     */
    private static Specification<Booking> byScheduleId(UUID scheduleId) {
        return (root, query, criteriaBuilder) -> {
            if (scheduleId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("schedule").get("id"), scheduleId);
        };
    }

    /**
     * Filter by booking status.
     */
    private static Specification<Booking> byStatus(BookingFilterDTO.StatusEnum status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), BookingStatus.valueOf(status.toString()));
        };
    }

    /**
     * Filter by booking datetime range.
     */
    private static Specification<Booking> byBookingDatetimeRange(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null && to == null) {
                return criteriaBuilder.conjunction();
            }
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get(BOOK_DATE_TIME), from, to);
            }
            if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(BOOK_DATE_TIME), from);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get(BOOK_DATE_TIME), to);
        };
    }

    /**
     * Filter by creation date range.
     */
    private static Specification<Booking> byCreatedAtRange(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null && to == null) {
                return criteriaBuilder.conjunction();
            }
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get(CREATED_AT), from, to);
            }
            if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_AT), from);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get(CREATED_AT), to);
        };
    }

    /**
     * Filter by notification sent status.
     */
    private static Specification<Booking> byNotificationSent(Boolean notificationSent) {
        return (root, query, criteriaBuilder) -> {
            if (notificationSent == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("notificationSent"), notificationSent);
        };
    }
}
