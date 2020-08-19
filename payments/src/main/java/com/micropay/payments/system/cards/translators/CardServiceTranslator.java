package com.micropay.payments.system.cards.translators;

import com.micropay.payments.domains.Card;
import com.micropay.payments.domains.CardType;
import com.micropay.payments.system.cards.domains.CardsResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardServiceTranslator {

    public List<Card> translateFromResponse(CardsResponse response) {
        return response.getCards().stream()
                .map(card -> new Card(
                        card.getId(),
                        card.getNumber(),
                        CardType.valueOf(card.getType().toUpperCase()),
                        card.getExpiresOn(),
                        card.getUserId()))
                .collect(Collectors.toList());
    }
}
