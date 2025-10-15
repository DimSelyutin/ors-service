package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;

/**
 * PoolRepository for get entity.
 */
public interface PoolRepository extends JpaRepository<Pool, UUID>, JpaSpecificationExecutor<Pool> {

    Optional<Pool> findPoolById(UUID id);
}


