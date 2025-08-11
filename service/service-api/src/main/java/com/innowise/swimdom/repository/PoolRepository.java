package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for pools.
 */
@Repository
public interface PoolRepository
    extends JpaRepository<Pool, UUID>, JpaSpecificationExecutor<Pool> {

    Pool save(Pool pool);

    Optional<Pool> findPoolById(UUID id);

    Optional<List<Pool>> findPoolsByPoolWorkingHoursWeekday(Short weekday);

    boolean existsById(UUID poolId);
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
}
