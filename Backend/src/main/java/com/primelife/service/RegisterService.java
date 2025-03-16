package com.primelife.service;

import com.primelife.exception.EmailException;
import com.primelife.exception.RegisterException;
import com.primelife.exception.TokenException;
import com.primelife.request.RegisterRequest;

public interface RegisterService {
    void registerNewUser(RegisterRequest registerRequest, String siteURL) throws RegisterException, EmailException;

    void verifyNewUser(String verificationToken) throws TokenException, EmailException, RegisterException;
}
