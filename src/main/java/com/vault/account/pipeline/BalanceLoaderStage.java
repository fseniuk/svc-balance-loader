package com.vault.account.pipeline;

import com.vault.account.model.BalanceLoaderMessage;

@FunctionalInterface
public interface BalanceLoaderStage {

    BalanceLoaderMessage process(BalanceLoaderMessage inputMsg);
}
