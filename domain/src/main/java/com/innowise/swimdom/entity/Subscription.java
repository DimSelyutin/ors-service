package com.innowise.swimdom.entity;

import com.innowise.swimdom.enums.SubscriptionDuration;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * Entity for pools.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "subscription",
      uniqueConstraints = @UniqueConstraint(name = "uq_subscription_name", columnNames = "name"))
public class Subscription extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT",  length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "duration", nullable = false, columnDefinition = "subscription_duration")
    private SubscriptionDuration duration;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
}
