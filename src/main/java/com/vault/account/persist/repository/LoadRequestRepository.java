package com.vault.account.persist.repository;

import com.vault.account.persist.entity.LoadRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoadRequestRepository extends JpaRepository<LoadRequestEntity, String> {

    Optional<LoadRequestEntity> findByIdAndCustomerId(String id, String customerId);
}
