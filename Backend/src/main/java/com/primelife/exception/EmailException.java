package com.primelife.exception;

import lombok.Getter;

@Getter
public class EmailException extends Exception {

    private final int statusCode;

    public EmailException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}

