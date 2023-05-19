package com.vault.account;

import com.vault.account.model.request.LoadRequestMessage;
import com.vault.account.persist.entity.LoadRequestEntity;
import com.vault.account.persist.repository.LoadRequestRepository;
import com.vault.account.pipeline.RequestValidationStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class RequestValidationStageTest {

    @Mock
    private LoadRequestRepository requestRepo;

    @Autowired
    @InjectMocks
    private RequestValidationStage validationStage;

    private final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    private final LoadRequestMessage loadReq = LoadRequestMessage.builder()
            .id("123")
            .customerId("908")
            .time(zonedDateTime)
            .amount("$3672.98")
            .build();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void process_receivesLoadRequestMessage_shouldSucceedAndReturnSameRequest() {
        doReturn(Optional.empty()).when(requestRepo).findByIdAndCustomerId("123", "908");

        LoadRequestMessage reqResponse = (LoadRequestMessage) validationStage.process(loadReq);

        assertNotNull(reqResponse);
    }

    @Test
    public void process_receivesRepeatedLoadRequestMessage_shouldFailValidationAndReturnNull() {
        LoadRequestEntity loadRequestEntity = LoadRequestEntity.builder()
                .id("123")
                .customerId("908")
                .receivedAt(Timestamp.valueOf(zonedDateTime.toLocalDateTime()))
                .build();
        Optional<LoadRequestEntity> lookedForRequest = Optional.of(loadRequestEntity);

        doReturn(lookedForRequest).when(requestRepo).findByIdAndCustomerId("123", "908");

        LoadRequestMessage reqResponse = (LoadRequestMessage) validationStage.process(loadReq);

        assertNull(reqResponse);
    }
}
