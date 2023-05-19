package com.vault.account.model.entityid;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class WeekVelocityId implements Serializable {

    private int currentYear;
    private int weekOfYear;
    private String customerId;
    private short successfulLoadAttempt;
}
