package com.innowise.swimdom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalTime;

/**
 * Class of entity PoolWorkingHours.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "pool_working_hours",
       uniqueConstraints = @UniqueConstraint(name = "uq_pool_working_hours_pool_weekday",
                                             columnNames = {"pool_id", "weekday"}))
public class PoolWorkingHours extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pool_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_pool_working_hours_pool"))
    private Pool pool;

    @Column(name = "weekday", nullable = false)
    private Short weekday;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;
}

