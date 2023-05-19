package com.vault.account.pipeline;

import com.vault.account.model.BalanceLoaderMessage;
import com.vault.account.persist.entity.BalanceEntity;
import com.vault.account.persist.entity.LoadRequestEntity;
import com.vault.account.persist.entity.TodayVelocityEntity;
import com.vault.account.persist.entity.WeekVelocityEntity;
import com.vault.account.persist.repository.BalanceRepository;
import com.vault.account.model.pipeline.EntityBuildingResponse;
import com.vault.account.model.pipeline.WeekVelocityLimitingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@Component
@Slf4j
public class EntityBuildingStage implements BalanceLoaderStage {

    @Autowired
    private BalanceRepository balanceRepo;

    @Override
    public BalanceLoaderMessage process(BalanceLoaderMessage inputMsg) {
        log.info("Building final entities for persisting...");

        WeekVelocityLimitingResponse weekResp = (WeekVelocityLimitingResponse) inputMsg;

        log.debug("Building BalanceEntity...");
        BalanceEntity resultBalanceEntity;
        String customerId = weekResp.getDayVelocityLimitingResponse().getCustomerId();

        log.info("Retrieving balance info for customer {}", customerId);
        Optional<BalanceEntity> currentBalance = balanceRepo.findById(customerId);
        if(currentBalance.isEmpty()) {
            log.info("No balance info found for customer {}", customerId);
            resultBalanceEntity = BalanceEntity.builder()
                    .customerId(customerId)
                    .balance(weekResp.getDayVelocityLimitingResponse().getRequestLoadAmount().toPlainString())
                    .build();
        }
        else {
            log.info("Balance info found for customer {}", customerId);
            BigDecimal incrementedAbsoluteBalance = weekResp.getDayVelocityLimitingResponse().getRequestLoadAmount()
                    .add(new BigDecimal(currentBalance.get().getBalance()));
            resultBalanceEntity = BalanceEntity.builder()
                    .customerId(customerId)
                    .balance(incrementedAbsoluteBalance.toPlainString())
                    .build();
        }
        log.debug("BalanceEntity build complete");

        log.debug("Building LoadRequestEntity...");
        Timestamp receivedAt = Timestamp.valueOf(weekResp.getDayVelocityLimitingResponse().getTime().toLocalDateTime());
        LoadRequestEntity loadRequestEntity = LoadRequestEntity.builder()
                .id(weekResp.getDayVelocityLimitingResponse().getRequestId())
                .customerId(customerId)
                .receivedAt(receivedAt)
                .build();
        log.debug("LoadRequestEntity build complete");

        log.debug("Building TodayVelocityEntity...");
        TodayVelocityEntity newTodayVelocityEntity = TodayVelocityEntity.builder()
                .customerId(customerId)
                .loadedOn(weekResp.getDayVelocityLimitingResponse().getDayDate())
                .successfulLoadAttempt(weekResp.getDayVelocityLimitingResponse().getDayIncrementedLoadAttempt())
                .cumulativeBalance(weekResp.getDayVelocityLimitingResponse().getDayIncrementedLoadBalance().toPlainString())
                .build();
        log.debug("TodayVelocityEntity build complete");

        log.debug("Building WeekVelocityEntity...");
        WeekVelocityEntity newWeekVelocityEntity = WeekVelocityEntity.builder()
                .currentYear(weekResp.getYear())
                .weekOfYear(weekResp.getWeekOfYear())
                .customerId(customerId)
                .successfulLoadAttempt(weekResp.getWeekIncrementedLoadAttempt())
                .cumulativeBalance(weekResp.getWeekIncrementedLoadBalance().toPlainString())
                .build();

        log.debug("WeekVelocityEntity build complete");

        log.info("Entities build complete");

        return EntityBuildingResponse.builder()
                .resultBalanceEntity(resultBalanceEntity)
                .loadRequestEntity(loadRequestEntity)
                .todayVelocityEntity(newTodayVelocityEntity)
                .weekVelocityEntity(newWeekVelocityEntity)
                .build();
    }
}
