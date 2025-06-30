package com.innowise.swimdom.entity;

import com.innowise.swimdom.enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity for pools.
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "booking",
       uniqueConstraints = @UniqueConstraint(name = "uq_booking_user_schedule",
                                             columnNames = {"user_id", "schedule_id"}))
public class Booking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_booking_users"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_subscription_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_subscription"))
    private Subscription userSubscription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_booking_schedule"))
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "booking_status")
    private BookingStatus status;

    @Column(name = "booking_datetime", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime bookingDatetime;

    @Column(name = "notification_sent", nullable = false)
    private Boolean notificationSent;

    @PrePersist
    public void onNotification_sent() {
        if (notificationSent == null) {
            notificationSent = false;
        }
    }
}

