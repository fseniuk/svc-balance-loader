package com.vault.account.model.pipeline;

import com.vault.account.model.BalanceLoaderMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class DayVelocityLimitingResponse implements BalanceLoaderMessage {

    private short dayIncrementedLoadAttempt;
    private BigDecimal dayIncrementedLoadBalance;
    private BigDecimal requestLoadAmount;
    private String customerId;
    private ZonedDateTime time;
    private String requestId;
    private Date dayDate;
}
