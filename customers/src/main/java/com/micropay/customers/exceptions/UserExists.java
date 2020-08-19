package com.micropay.customers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserExists extends RuntimeException {
    public UserExists(String message) {
        super(message);
    }
}
