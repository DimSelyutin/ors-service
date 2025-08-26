package com.innowise.swimdom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;

/**
 * Class of entity UserSubscription.
 */
@Data
@Entity
@Table(name = "user_subscription",
       uniqueConstraints = @UniqueConstraint(
           name = "uq_user_subscription_user_start_end",
           columnNames = {"user_id", "start_date", "end_date"}))
public class UserSubscription {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_subscription_users"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_subscription_subscription"))
    private Subscription subscription;

    @Column(name = "estimate", nullable = false)
    private Integer estimate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @PrePersist
    public void onStartAndEndDate() {
        if (startDate == null) {
            LocalDate now = LocalDate.now();
            int month = now.getMonth().getValue();
            int year = now.getYear();
            startDate = now;
            endDate = now.plusDays(subscription.getDuration()
                .getDurationInDays(month, year));
        }
    }

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void reduceSessions() {
        this.estimate = this.estimate >= 1 ? this.estimate - 1 : 0;
    }

    public void increaseSessions() {

        this.estimate = this.estimate >= 1 ? this.estimate + 1 : 0;
    }
}
