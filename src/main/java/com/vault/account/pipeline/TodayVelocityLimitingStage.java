package com.vault.account.pipeline;

import com.vault.account.model.BalanceLoaderMessage;
import com.vault.account.model.request.LoadRequestMessage;
import com.vault.account.persist.entity.TodayVelocityEntity;
import com.vault.account.persist.repository.TodayVelocityRepository;
import com.vault.account.pipeline.model.DayVelocityLimitingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Optional;

@Component
@Slf4j
public class TodayVelocityLimitingStage implements BalanceLoaderStage {

    private static final BigDecimal FIVE_THOUSAND = new BigDecimal("5000");

    @Autowired
    private TodayVelocityRepository todayVelocityRepo;

    @Override
    public BalanceLoaderMessage process(BalanceLoaderMessage inputMsg) {
        log.info("Running day limit checks...");
        LoadRequestMessage reqMsg = (LoadRequestMessage) inputMsg;
        Date dayDate = toDate(reqMsg.getTime());
        BigDecimal reqLoadAmount = new BigDecimal(removeDollarSign(reqMsg.getAmount()));

        log.info("Retrieving current day latest load info");
        String customerId = reqMsg.getCustomerId();
        Optional<TodayVelocityEntity> todayLatestLoad = todayVelocityRepo.findLatestByCustomerIdAndLoadedOn(customerId, dayDate);
        short todayIncrementedLoadAttempt = 0;
        BigDecimal todayIncrementedLoadBalance = BigDecimal.ZERO;
        if(todayLatestLoad.isPresent()) {
            log.info("Found current day latest load info");
            short latestTodayLoadAttempt = todayLatestLoad.get().getSuccessfulLoadAttempt();
            if(latestTodayLoadAttempt == 3) {
                log.warn("Load refused. Customer {} already made 3 loads in the current day", customerId);
                // return response with accepted = false
            }

            BigDecimal latestTodayLoadBalance = new BigDecimal(todayLatestLoad.get().getCumulativeBalance());
            todayIncrementedLoadBalance = latestTodayLoadBalance.add(new BigDecimal(reqMsg.getAmount()));
            if(FIVE_THOUSAND.compareTo(todayIncrementedLoadBalance) < 0) {
                log.warn("Load refused. Requested load of ${} would exceed the daily limit of ${}", todayIncrementedLoadBalance, FIVE_THOUSAND);
                // return response with accepted = false
            }

            todayIncrementedLoadAttempt = (short) (todayLatestLoad.get().getSuccessfulLoadAttempt() + 1);
        }
        else {
            log.info("No current day latest load info found. This is the first load in the current day");
            if(FIVE_THOUSAND.compareTo(reqLoadAmount) < 0) {
                log.warn("Load refused. Requested load of ${} would exceed the daily limit of ${}", reqLoadAmount, FIVE_THOUSAND);
                // return response with accepted = false
            }
            todayIncrementedLoadBalance = todayIncrementedLoadBalance.add(reqLoadAmount);
            todayIncrementedLoadAttempt++;
        }

        log.info("Day limit checks complete");

        return DayVelocityLimitingResponse.builder()
                .dayIncrementedLoadAttempt(todayIncrementedLoadAttempt)
                .dayIncrementedLoadBalance(todayIncrementedLoadBalance)
                .requestLoadAmount(reqLoadAmount)
                .requestId(reqMsg.getId())
                .customerId(customerId)
                .time(reqMsg.getTime())
                .dayDate(dayDate)
                .build();
    }

    private static Date toDate(ZonedDateTime zonedDateTime) {
        return Date.valueOf(zonedDateTime.toLocalDate());
    }

    private static String removeDollarSign(String s) {
        return s.replace("$", "");
    }
}
