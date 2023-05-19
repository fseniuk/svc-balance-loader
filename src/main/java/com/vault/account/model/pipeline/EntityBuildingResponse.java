package com.vault.account.model.pipeline;

import com.vault.account.model.BalanceLoaderMessage;
import com.vault.account.persist.entity.BalanceEntity;
import com.vault.account.persist.entity.LoadRequestEntity;
import com.vault.account.persist.entity.TodayVelocityEntity;
import com.vault.account.persist.entity.WeekVelocityEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EntityBuildingResponse implements BalanceLoaderMessage {

    private BalanceEntity resultBalanceEntity;
    private LoadRequestEntity loadRequestEntity;
    private TodayVelocityEntity todayVelocityEntity;
    private WeekVelocityEntity weekVelocityEntity;
}
