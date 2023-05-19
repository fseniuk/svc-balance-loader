package com.vault.account.persist.entity;

import com.vault.account.model.TodayVelocityId;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "today_velocity")
@IdClass(TodayVelocityId.class)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TodayVelocityEntity {

    @Id
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Id
    @Column(name = "loaded_on", nullable = false)
    private Date loadedOn;

    @Id
    @Column(name = "successful_load_attempt", nullable = false)
    private short successfulLoadAttempt;

    @EqualsAndHashCode.Exclude
    @Column(name = "cumulative_balance", nullable = false)
    private String cumulativeBalance;
}
