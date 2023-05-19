package com.vault.account.persist.entity;

import com.vault.account.model.entityid.WeekVelocityId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "week_velocity")
@IdClass(WeekVelocityId.class)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WeekVelocityEntity {

    @Id
    @Column(name = "current_year", nullable = false)
    private int currentYear;

    @Id
    @Column(name = "week_of_year", nullable = false)
    private int weekOfYear;

    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Id
    @Column(name = "successful_load_attempt", nullable = false)
    private short successfulLoadAttempt;

    @EqualsAndHashCode.Exclude
    @Column(name = "cumulative_balance", nullable = false)
    private String cumulativeBalance;
}
