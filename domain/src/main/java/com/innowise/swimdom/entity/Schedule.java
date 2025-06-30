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
import java.time.LocalDateTime;

/**
 * Entity for pools.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "schedule",
       uniqueConstraints = @UniqueConstraint(name = "uq_schedule_pool_start",
                                             columnNames = {"pool_id", "start_datetime"}))
public class Schedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pool_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_schedule_pool"))
    private Pool pool;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

}
