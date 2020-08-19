package com.micropay.cards.testUtils;

import com.micropay.cards.domains.Card;
import com.micropay.cards.domains.CardType;
import com.micropay.cards.domains.User;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestData {
    public static Card getValidCard() {
        Card card = new Card();
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setCards(Arrays.asList(card));
        card.setUser(user);
        card.setId(123L);
        card.setExpiresOn(LocalDate.now());
        card.setNumber(BigInteger.ONE);
        card.setType(CardType.MASTER_CARD);
        return card;
    }

    public static List<Card> getCardsList(){
        return Arrays.asList(getValidCard());
    }

    public static User getValidUser(){
        User user = new User();
        user.setName("name");
        user.setId(1L);
        user.addCard(getValidCard());
        return user;
    }
}