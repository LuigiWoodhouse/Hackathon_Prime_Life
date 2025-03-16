package com.primelife.service;

import com.primelife.exception.KeyVaultException;

public interface KeyFetchService {
    String fetchDbUsername() throws KeyVaultException;

    String fetchDbPassword() throws KeyVaultException;

    String fetchEmailUsername() throws KeyVaultException;

    String fetchEmailPassword() throws KeyVaultException;


    String fetchJwtPrivateKey() throws KeyVaultException;

    String fetchJwtPublicKey() throws KeyVaultException;
}
