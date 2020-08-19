package com.micropay.payments.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoOpenTransactionException extends RuntimeException {
    public NoOpenTransactionException(String message) {
        super(message);
    }
}
