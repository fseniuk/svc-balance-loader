package com.vault.account.model.entityid;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@EqualsAndHashCode
public class TodayVelocityId implements Serializable {

    private Date loadedOn;
    private short successfulLoadAttempt;
    private String customerId;
}
