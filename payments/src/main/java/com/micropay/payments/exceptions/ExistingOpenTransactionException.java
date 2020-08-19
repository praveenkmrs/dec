package com.micropay.payments.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistingOpenTransactionException extends RuntimeException {
    public ExistingOpenTransactionException(String message) {
        super(message);
    }
}
