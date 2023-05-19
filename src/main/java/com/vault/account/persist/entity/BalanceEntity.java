package com.vault.account.persist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "balance")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BalanceEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "balance", nullable = false)
    private String balance;
}
