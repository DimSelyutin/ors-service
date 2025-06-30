package com.innowise.swimdom.enums;

import java.util.Calendar;

/**
 * Enum of subscription duration.
 */
public enum SubscriptionDuration {

    /**
     * Subscription on week month.
     */
    WEEK(7),

    /**
     * Subscription on month.
     */
    MONTH,

    /**
     * Subscription on year.
     */
    YEAR(365);

    /**
     * Subscription constructor with params on year
     *
     * @param days count days.
     */
    SubscriptionDuration(int days) {
        this.days = days;
    }

    /**
     * Subscription constructor without params on year.
     */
    SubscriptionDuration() {

    }

    /**
     * count of days.
     */
    private Integer days;

    /**
     * @param month number of month
     * @param year  number of year
     * @return count days.
     * For use: SubscriptionDuration.MONTH.getDurationInDays(month, year).
     */
    public int getDurationInDays(int month, int year) {
        if (this == MONTH) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        return days != null ? days : 0;
    }
}
