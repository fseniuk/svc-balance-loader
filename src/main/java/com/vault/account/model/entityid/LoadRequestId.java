package com.vault.account.model.entityid;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class LoadRequestId implements Serializable {

    private String id;
    private String customerId;
}
