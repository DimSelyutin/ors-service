package com.innowise.swimdom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for pools.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "pools")
public class Pool extends BaseEntity {

    @Column(nullable = false, length = 150, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 255, nullable = false, unique = true)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

}
