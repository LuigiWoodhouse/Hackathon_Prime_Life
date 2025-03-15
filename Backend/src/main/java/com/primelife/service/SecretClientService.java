package com.primelife.service;

import com.azure.security.keyvault.secrets.SecretClient;
import com.primelife.exception.KeyVaultException;


public interface SecretClientService {
    SecretClient fetchSecretClient() throws KeyVaultException;
}