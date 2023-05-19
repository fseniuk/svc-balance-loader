package com.vault.account.service;

import com.vault.account.model.BalanceLoaderMessage;
import com.vault.account.model.request.LoadRequestMessage;
import com.vault.account.model.response.LoadResponseMessage;
import com.vault.account.pipeline.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class BalanceLoaderService {

    @Autowired
    private RequestValidationStage requestValidationStage;
    @Autowired
    private TodayVelocityLimitingStage todayVelocityLimitingStage;
    @Autowired
    private WeekVelocityLimitingStage weekVelocityLimitingStage;
    @Autowired
    private EntityBuildingStage entityBuildingStage;
    @Autowired
    private PersistenceStage persistenceStage;

    private List<BalanceLoaderStage> pipeline;

    @PostConstruct
    public void buildPipeline() {
        pipeline = Collections.unmodifiableList(Arrays.asList(
                requestValidationStage,
                todayVelocityLimitingStage,
                weekVelocityLimitingStage,
                entityBuildingStage,
                persistenceStage
        ));
    }

    public LoadResponseMessage load(LoadRequestMessage reqMsg) {
        BalanceLoaderMessage msg = reqMsg;
        boolean accepted = true;
        try {
            for(BalanceLoaderStage stage : pipeline) {
                msg = stage.process(msg);
                if(msg == null && !(stage instanceof PersistenceStage)) {
                    accepted = false;
                    break;
                }
            }
        } catch (Throwable ex) {
            accepted = false;
            // would be easy to return an error message using the exception message
        }
        finally {
            // any operation which must happen
            // ex.: call method postProcess (to be implemented) from the pipeline stages
        }

        return LoadResponseMessage.builder()
                .id(reqMsg.getId())
                .customerId(reqMsg.getCustomerId())
                .accepted(accepted)
                .build();
    }
}
