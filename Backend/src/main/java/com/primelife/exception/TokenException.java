package com.primelife.exception;

import lombok.Getter;

@Getter
public class TokenException extends Exception {

    private final int statusCode;

    public TokenException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}