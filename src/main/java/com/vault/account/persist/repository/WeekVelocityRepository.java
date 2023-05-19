package com.vault.account.persist.repository;

import com.vault.account.model.entityid.WeekVelocityId;
import com.vault.account.persist.entity.WeekVelocityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeekVelocityRepository extends JpaRepository<WeekVelocityEntity, WeekVelocityId> {

    @Query(nativeQuery = true,
            value = "SELECT current_year, week_of_year, customer_id, MAX(successful_load_attempt) successful_load_attempt, cumulative_balance"
                    + " FROM week_velocity"
                    + " WHERE current_year = ?1 AND week_of_year = ?2 AND customer_id = ?3"
                    + " GROUP BY successful_load_attempt")
    Optional<WeekVelocityEntity> findLatestByYearAndWeekOfYearAndCustomerId(int year, int weekOfYear, String customerId);
}
