package com.micropay.payments.system.cards.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardRequest implements Serializable {
    @NotNull
    private String number;
    @NotNull
    private String type;
    @NotNull
    private String expiresOn;
}
