package com.primelife.exception;

import lombok.Getter;

@Getter
public class KeyException extends Exception {

    private final int statusCode;

    public KeyException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}