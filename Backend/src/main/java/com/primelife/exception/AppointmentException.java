package com.primelife.exception;


import lombok.Getter;

@Getter
public class AppointmentException extends Exception {

    private final int statusCode;

    public AppointmentException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
