package com.micropay.cards.resource.translators;

import com.micropay.cards.domains.CardType;
import com.micropay.cards.resource.contract.Card;
import com.micropay.cards.resource.contract.CardRequest;
import com.micropay.cards.resource.contract.CardResponse;
import com.micropay.cards.resource.contract.CardsResponse;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestResourceTranslator {

    public CardsResponse translateToContract(List<com.micropay.cards.domains.Card> cards) {
        return CardsResponse.builder()
                .cards(cards.stream()
                        .map(card -> Card.builder()
                                .id(card.getId())
                                .expiresOn(card.getExpiresOn())
                                .number(card.getNumber())
                                .type(card.getType().getValue())
                                .userId(card.getUser().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public CardResponse translateToContract(com.micropay.cards.domains.Card card) {
        return CardResponse.builder()
                .card(Card.builder()
                        .id(card.getId())
                        .expiresOn(card.getExpiresOn())
                        .number(card.getNumber())
                        .type(card.getType().getValue())
                        .userId(card.getUser().getId())
                        .build())
                .build();
    }

    public com.micropay.cards.domains.Card translateToDomain(CardRequest request) {
        return new com.micropay.cards.domains.Card(
                new BigInteger(request.getNumber()),
                CardType.valueOf(request.getType().toUpperCase()),
                LocalDate.parse(request.getExpiresOn()));
    }

}