package com.vault.account.pipeline.model;

import com.vault.account.model.BalanceLoaderMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class WeekVelocityLimitingResponse implements BalanceLoaderMessage {

    private short weekIncrementedLoadAttempt;
    private BigDecimal weekIncrementedLoadBalance;
    private DayVelocityLimitingResponse dayVelocityLimitingResponse;
    private int year;
    private int weekOfYear;
}
