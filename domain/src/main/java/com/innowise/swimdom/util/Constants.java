package com.innowise.swimdom.util;

import java.util.List;

/**
 * Class with constants.
 */
public final class Constants {

    private Constants() {
    }

    public static final String SQLQUERY = """
        ELECT EXISTS (SELECT 1 FROM schedule s JOIN pool p ON s.pool_id = p.id
        JOIN pool_working_hours pw ON pw.pool_id = p.id WHERE p.id = :id
        AND s.start_datetime <= :closeTime AND pw.open_time < :openTime
        AND pw.close_time > :closeTime) AS exists_flag""";

    public static final List<String> PUBLIC_URLS = List.of(
        "/api/v1/auth/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/actuator"
    );

    public static final List<String> ADMIN_URLS = List.of(
        "api/v1/admin/**"
    );
    public static final List<String> ALLOWED_ORIGIN_PATTERNS = List.of("*");

    public static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");

    public static final List<String> ALLOWED_HEADERS = List.of("*");

    public static final String INVALID_CREDENTIAL = "Wrong login or password!";
    public static final String TOKEN_INVALID = "Token expired or invalid!";
    public static final String ROLE_PREFIX = "ROLE_";

    public static final String USER_NOT_FOUND = "User not found with ID: ";
    public static final String BOOKING_NOT_FOUND = "Booking not found with ID: ";
    public static final String POOL_NOT_FOUND = "Pool not found with ID: ";

    public static final String BOOKING_ALREADY_EXIST =
        "User already has a booking for this schedule. Please choose a different time slot.";
    public static final String TIME_NOT_AVAILABLE =
        "The selected time slot is not available or pool is at full capacity.";
    public static final String BOOKING_TIME_OUTSIDE =
        "Booking time is outside of pool's operating hours.";
    public static final String USER_SUBSCRIPTION_NOT_BELONG =
        "User subscription does not belong to the user.";
    public static final String USER_SUBSCRIPTION_NOT_FOUND =
        "User subscription not found with ID: ";
    public static final String SCHEDULE_NOT_FOUND =
        "Schedule not found with ID: ";
    public static final String USER_SUBSCRIPTION_EXPIRED =
        "User subscription has expired.";
    public static final String BOOKING_DATE_OUTSIDE =
        "Booking date is outside of subscription period.";
    public static final String SCHEDULE_NOT_AVAILABLE =
        "The new schedule is not available or pool is at full capacity.";

    public static final String CONFIRMED = "CONFIRMED";
}
