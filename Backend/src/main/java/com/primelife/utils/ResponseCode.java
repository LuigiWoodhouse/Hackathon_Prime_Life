package com.primelife.utils;

import lombok.Getter;

@Getter
public enum ResponseCode {
    OPERATION_FAILED("RESP001"),
    INVALID_EMAIL_FORMAT("RESP002");

    private final String code;

    ResponseCode(String code) {
        this.code = code;
    }
}