package com.micropay.payments.system.cards.domains.meta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micropay.payments.system.cards.domains.CardsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {
    private Meta meta;
    private CardsResponse response;
}
