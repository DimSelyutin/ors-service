package com.innowise.swimdom.entity;

import com.innowise.swimdom.util.ValidOpenCloseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Class of entity PoolWorkingHours.
 */
@ValidOpenCloseTime
@Data
@Entity
@Table(name = "pool_working_hours",
       uniqueConstraints = @UniqueConstraint(name = "uq_pool_working_hours_pool_weekday",
                                             columnNames = {"pool_id", "weekday"}))
public class PoolWorkingHours {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pool_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_pool_working_hours_pool"))
    private Pool pool;

    @Min(1)
    @Max(7)
    @Column(name = "weekday", nullable = false)
    private Short weekday;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

