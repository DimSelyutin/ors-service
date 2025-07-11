package com.innowise.swimdom.mapper;

import com.innowise.swimdom.enums.SubscriptionDuration;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionDurationTest {

    @Test
    void getDurationInDays_Week_Returns7() {
        assertEquals(7, SubscriptionDuration.WEEK.getDurationInDays(1, 2024));
    }

    @Test
    void getDurationInDays_Year_Returns365() {
        assertEquals(365, SubscriptionDuration.YEAR.getDurationInDays(1, 2024));
    }

    @Test
    void getDurationInDays_Month_ReturnsCorrectDays() {
        // Check February 2024 (leap year) - 29 days
        int daysFeb2024 = SubscriptionDuration.MONTH.getDurationInDays(2, 2024);
        assertEquals(29, daysFeb2024);

        // Check April 2024-30 days
        int daysApr2024 = SubscriptionDuration.MONTH.getDurationInDays(4, 2024);
        assertEquals(30, daysApr2024);

        // Check January 2024-31 days
        int daysJan2024 = SubscriptionDuration.MONTH.getDurationInDays(1, 2024);
        assertEquals(31, daysJan2024);
    }
}
