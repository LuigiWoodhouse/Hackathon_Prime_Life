package com.primelife.exception;

import lombok.Data;

@Data
public class RegisterException extends Exception {

    private static final long serialVersionUID = 1L;

    private int code;

    public RegisterException(int code, String message) {
        super(message);
        this.code = code;
    }
}
