package com.vault.account.persist.repository;

import com.vault.account.model.TodayVelocityId;
import com.vault.account.persist.entity.TodayVelocityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.Optional;

public interface TodayVelocityRepository extends JpaRepository<TodayVelocityEntity, TodayVelocityId> {

    @Query(nativeQuery = true,
            value = "SELECT customer_id, loaded_on, MAX(successful_load_attempt) successful_load_attempt, cumulative_balance"
                    + " FROM today_velocity"
                    + " WHERE customer_id = ?1 AND loaded_on = ?2"
                    + " GROUP BY successful_load_attempt")
    Optional<TodayVelocityEntity> findLatestByCustomerIdAndLoadedOn(String customerId, Date loadedOn);
}
