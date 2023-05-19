package com.vault.account.pipeline;

import com.vault.account.model.BalanceLoaderMessage;
import com.vault.account.model.request.LoadRequestMessage;
import com.vault.account.persist.entity.LoadRequestEntity;
import com.vault.account.persist.repository.LoadRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class RequestValidationStage implements BalanceLoaderStage {

    @Autowired
    private LoadRequestRepository requestRepo;

    @Override
    public BalanceLoaderMessage process(BalanceLoaderMessage inputMsg) {
        LoadRequestMessage reqMsg = (LoadRequestMessage) inputMsg;

        log.info("Validating load request with id={}...", reqMsg.getId());

        Optional<LoadRequestEntity> lookedForRequest = requestRepo.findByIdAndCustomerId(reqMsg.getId(), reqMsg.getCustomerId());
        if(lookedForRequest.isPresent()) {
            log.warn("Request id already used for this user. This request will be ignored");
            return null;
        }

        log.info("Validation complete for request with id={}", reqMsg.getId());

        return reqMsg;
    }
}
