package com.primelife.exception;

import lombok.Getter;

@Getter
public class KeyVaultException extends Exception {

    private final int statusCode;

    public KeyVaultException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}