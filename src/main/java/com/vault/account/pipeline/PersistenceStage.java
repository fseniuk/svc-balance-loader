package com.vault.account.pipeline;

import com.vault.account.model.BalanceLoaderMessage;
import com.vault.account.persist.repository.BalanceRepository;
import com.vault.account.persist.repository.LoadRequestRepository;
import com.vault.account.persist.repository.TodayVelocityRepository;
import com.vault.account.persist.repository.WeekVelocityRepository;
import com.vault.account.pipeline.model.EntityBuildingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersistenceStage implements BalanceLoaderStage {

    @Autowired
    private LoadRequestRepository requestRepo;
    @Autowired
    private BalanceRepository balanceRepo;
    @Autowired
    private TodayVelocityRepository todayVelocityRepo;
    @Autowired
    private WeekVelocityRepository weekVelocityRepo;

    @Override
    public BalanceLoaderMessage process(BalanceLoaderMessage inputMsg) {
        EntityBuildingResponse entityBuildResp = (EntityBuildingResponse) inputMsg;

        log.info("Saving final entities...");
        requestRepo.saveAndFlush(entityBuildResp.getLoadRequestEntity());
        balanceRepo.saveAndFlush(entityBuildResp.getResultBalanceEntity());
        todayVelocityRepo.saveAndFlush(entityBuildResp.getTodayVelocityEntity());
        weekVelocityRepo.saveAndFlush(entityBuildResp.getWeekVelocityEntity());
        log.info("Entities saved successfully");

        return null;
    }
}
