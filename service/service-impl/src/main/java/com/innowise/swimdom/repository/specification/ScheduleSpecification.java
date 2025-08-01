package com.innowise.swimdom.repository.specification;

import com.innowise.swimdom.entity.Schedule;
import com.innowise.swimdom.openapi.model.ScheduleDto;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Specification for Schedule entity filtering.
 */
public class ScheduleSpecification {

    /**
     * Creates a specification based on filter criteria.
     *
     * @param filterDTO the filter criteria
     * @return the specification
     */
    public static Specification<Schedule> byFilter(ScheduleDto filterDTO) {
        return Specification.where(byPoolId(filterDTO.getPoolId()))
                .and(byStartDatetimeAfter(filterDTO.getStartDatetime()))
                .and(byEndDatetimeBefore(filterDTO.getEndDatetime()));
    }

    /**
     * Specification for filtering by pool ID.
     */
    public static Specification<Schedule> byPoolId(UUID poolId) {
        return (root, query, criteriaBuilder) -> {
            if (poolId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("pool").get("id"), poolId);
        };
    }

    /**
     * Specification for filtering by start datetime after.
     */
    public static Specification<Schedule> byStartDatetimeAfter(LocalDateTime startDatetime) {
        return (root, query, criteriaBuilder) -> {
            if (startDatetime == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("startDatetime"), startDatetime);
        };
    }

    /**
     * Specification for filtering by end datetime before.
     */
    public static Specification<Schedule> byEndDatetimeBefore(LocalDateTime endDatetime) {
        return (root, query, criteriaBuilder) -> {
            if (endDatetime == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("endDatetime"), endDatetime);
        };
    }

    /**
     * Specification for filtering by time range.
     */
    public static Specification<Schedule> byTimeRange(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null || to == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDatetime"), from),
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDatetime"), to)
            );
        };
    }
}
