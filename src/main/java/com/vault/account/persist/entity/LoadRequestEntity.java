package com.vault.account.persist.entity;

import com.vault.account.model.entityid.LoadRequestId;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@IdClass(LoadRequestId.class)
@Table(name = "load_request")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LoadRequestEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Id
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @EqualsAndHashCode.Exclude
    @Column(name = "received_at", nullable = false)
    private Timestamp receivedAt;
}
