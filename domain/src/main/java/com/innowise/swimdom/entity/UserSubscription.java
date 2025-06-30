package com.innowise.swimdom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Class of entity UserSubscription.
 */
@Entity
@Table(name = "user_subscription",
       uniqueConstraints = @UniqueConstraint(
           name = "uq_user_subscription_user_start_end",
           columnNames = {"user_id", "start_date", "end_date"}))
@Getter
@Setter
public class UserSubscription extends BaseEntity {

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
    public void onStartDate() {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
    }

}
