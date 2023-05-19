package com.vault.account.model.response;

import com.vault.account.model.AbstractMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LoadResponseMessage extends AbstractMessage {

    private boolean accepted;
}
