package com.innowise.swimdom.repository;

import com.innowise.swimdom.entity.PoolWorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Repository for pools.
 */
@Repository
public interface PoolWorkingHoursRepository extends JpaRepository<UUID, PoolWorkingHours> {

    Optional<Set<PoolWorkingHours>> findByPoolId(UUID poolId);

    List<PoolWorkingHours> findByDayOfWeek(Short dayOfWeek);

    Set<PoolWorkingHours> saveAll(Set<PoolWorkingHours> poolWorkingHours);

    void deleteAllByPoolId(UUID poolId);

    @Modifying
    @Query("delete from PoolWorkingHours pwh where pwh.pool.id = :poolId and pwh.weekday = :dayOfWeek")
    void deleteByPoolIdAndWeekday(UUID poolId, Integer dayOfWeek);
}
