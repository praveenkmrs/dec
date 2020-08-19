package com.micropay.cards.domains;

import java.io.Serializable;

public enum CardType implements Serializable {
    AMEX("amex"),
    MASTER_CARD("master_card"),
    VISA("visa");

    private String value;

    CardType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
