package com.primelife.service;


import com.primelife.exception.KeyException;

import java.security.PrivateKey;
import java.security.PublicKey;


public interface KeyCreationService {
    PrivateKey generatePrivateKey(String privateKeyStr) throws KeyException;

    PublicKey generatePublicKey(String publicKeyStr) throws KeyException;
}
