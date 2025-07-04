package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for pools.
 */
@Repository
public interface PoolRepository
    extends JpaRepository<JpaSpecificationExecutor<Pool>, UUID>, JpaSpecificationExecutor<Pool> {

    Pool save(Pool pool);

    Optional<Pool> findPoolById(UUID id);

    Optional<List<Pool>> findPoolsByDayOfWeek(Short weekday);

    boolean existsById(UUID poolId);

    @Modifying
    @Query("delete from PoolWorkingHours pwh where pwh. = :poolId and pwh.dayOfWeek = :dayOfWeek")
    void deleteByPoolIdAndDayOfWeek(@Param("poolId") UUID poolId, @Param("dayOfWeek") Integer dayOfWeek);

}
