package com.innowise.swimdom.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Entity for pools.
 */
@Data
@Entity
@Table(name = "pool")
public class Pool {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "pool", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PoolWorkingHours> poolWorkingHours;

    @Column(nullable = false, length = 150, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 255, nullable = false, unique = true)
    private String location;

    @Column(nullable = false)
    @Min(value = 0, message = "capacity must be greater then zero")
    private Integer capacity;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
