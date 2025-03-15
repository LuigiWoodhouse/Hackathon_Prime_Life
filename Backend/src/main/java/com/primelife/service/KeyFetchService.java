package com.primelife.service;

import com.primelife.exception.KeyVaultException;

public interface KeyFetchService {
    String fetchDbUsername() throws KeyVaultException;

    String fetchDbPassword() throws KeyVaultException;
}
