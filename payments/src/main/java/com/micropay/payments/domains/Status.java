package com.micropay.payments.domains;

import java.io.Serializable;

public enum Status implements Serializable {
    OPEN("open"),
    CLOSED("closed"),
    FAILURE("failure");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
