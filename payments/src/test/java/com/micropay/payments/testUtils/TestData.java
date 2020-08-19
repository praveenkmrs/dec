package com.micropay.payments.testUtils;

import com.micropay.payments.domains.Card;
import com.micropay.payments.domains.CardType;
import com.micropay.payments.domains.Status;
import com.micropay.payments.domains.Transaction;

import java.math.BigInteger;
import java.time.LocalDate;

public class TestData {
    public static Transaction getValidOpenTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(getValidDomainCard());
        transaction.setStatus(Status.OPEN);
        transaction.setPaymentAmount(123);
        return transaction;
    }

    public static Transaction getValidCloseTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(getValidDomainCard());
        transaction.setStatus(Status.CLOSED);
        transaction.setPaymentAmount(123);
        return transaction;
    }

    public static Card getValidDomainCard(){
        return new Card(123L, BigInteger.TEN, CardType.MASTER_CARD, LocalDate.parse("2026-12-25"), 1L);
    }
    public static com.micropay.payments.system.cards.domains.Card getValidCard(){
        return com.micropay.payments.system.cards.domains.Card.builder()
                .expiresOn(LocalDate.parse("2026-12-25"))
                .id(123L)
                .number(BigInteger.TEN)
                .type(CardType.MASTER_CARD.getValue())
                .userId(1L)
                .build();
    }
}