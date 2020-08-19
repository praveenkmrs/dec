package com.micropay.cards.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CardAlreadyExistsException extends RuntimeException{
    public CardAlreadyExistsException(String message) {
        super(message);
    }
}
