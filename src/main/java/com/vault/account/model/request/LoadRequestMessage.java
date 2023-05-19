package com.vault.account.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vault.account.model.AbstractMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequestMessage extends AbstractMessage {

    @JsonProperty("load_amount")
    private String amount;
    private ZonedDateTime time;

    @Override
    public String toString() {
        return super.toString();
    }
}
