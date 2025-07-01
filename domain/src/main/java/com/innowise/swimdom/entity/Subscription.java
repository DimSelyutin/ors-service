package com.innowise.swimdom.entity;

import com.innowise.swimdom.enums.SubscriptionDuration;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for subscription.
 */
@Data
@Entity
@Table(name = "subscription",
      uniqueConstraints = @UniqueConstraint(name = "uq_subscription_name", columnNames = "name"))
public class Subscription {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT",  length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "duration", nullable = false, columnDefinition = "subscription_duration")
    private SubscriptionDuration duration;

    @Column(name = "price", precision = 10, scale = 2)
    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 digits in integer part and 2 digits in fractional part")
    private BigDecimal price;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
