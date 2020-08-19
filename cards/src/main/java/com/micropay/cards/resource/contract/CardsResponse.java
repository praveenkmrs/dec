package com.micropay.cards.resource.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micropay.cards.resource.contract.meta.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardsResponse extends Response implements Serializable {
    private List<Card> cards;
}
