package com.vault.account.pipeline;

import com.vault.account.model.BalanceLoaderMessage;
import com.vault.account.persist.entity.WeekVelocityEntity;
import com.vault.account.persist.repository.WeekVelocityRepository;
import com.vault.account.model.pipeline.DayVelocityLimitingResponse;
import com.vault.account.model.pipeline.WeekVelocityLimitingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

@Component
@Slf4j
public class WeekVelocityLimitingStage implements BalanceLoaderStage {

    private static final BigDecimal TWENTY_THOUSAND = new BigDecimal("20000");
    private static final String UTC = "UTC";
    private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC));

    @Autowired
    private WeekVelocityRepository weekVelocityRepo;

    @Override
    public BalanceLoaderMessage process(BalanceLoaderMessage inputMsg) {
        DayVelocityLimitingResponse dayResp = (DayVelocityLimitingResponse) inputMsg;

        log.info("Running week limit checks...");

        int year = calendar.get(Calendar.YEAR);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        log.info("Retrieving current week latest load info");
        Optional<WeekVelocityEntity> weekLatestLoad = weekVelocityRepo.findLatestByYearAndWeekOfYearAndCustomerId(
                year, weekOfYear, dayResp.getCustomerId()
        );

        short weekIncrementedLoadAttempt = 0;
        BigDecimal weekIncrementedLoadBalance = BigDecimal.ZERO;
        if(weekLatestLoad.isPresent()) {
            log.info("Found current week latest load info");
            BigDecimal latestWeekLoadBalance = new BigDecimal(weekLatestLoad.get().getCumulativeBalance());
            weekIncrementedLoadBalance = latestWeekLoadBalance.add(dayResp.getRequestLoadAmount());
            if(TWENTY_THOUSAND.compareTo(weekIncrementedLoadBalance) < 0) {
                log.warn("Load refused. Requested load of ${} would exceed the daily limit of ${}", weekIncrementedLoadBalance, TWENTY_THOUSAND);
                return null;
            }

            weekIncrementedLoadAttempt = (short) (weekLatestLoad.get().getSuccessfulLoadAttempt() + 1);
        }
        else {
            weekIncrementedLoadBalance = weekIncrementedLoadBalance.add(dayResp.getRequestLoadAmount());
            weekIncrementedLoadAttempt++;
        }

        log.info("Week limit checks complete");

        return WeekVelocityLimitingResponse.builder()
                .weekIncrementedLoadAttempt(weekIncrementedLoadAttempt)
                .weekIncrementedLoadBalance(weekIncrementedLoadBalance)
                .dayVelocityLimitingResponse(dayResp)
                .year(year)
                .weekOfYear(weekOfYear)
                .build();
    }
}
