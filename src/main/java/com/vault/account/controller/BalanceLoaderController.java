package com.vault.account.controller;

import com.vault.account.model.request.LoadRequestMessage;
import com.vault.account.model.response.LoadResponseMessage;
import com.vault.account.service.BalanceLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/load-balancer")
public class BalanceLoaderController {

    @Autowired
    private BalanceLoaderService svc;

    @PostMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoadResponseMessage load(@RequestBody LoadRequestMessage reqMsg) {
        return svc.load(reqMsg);
    }
}
