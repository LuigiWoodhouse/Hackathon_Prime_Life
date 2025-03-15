package com.primelife.service.impl;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.primelife.exception.KeyVaultException;
import com.primelife.service.KeyFetchService;
import com.primelife.service.SecretClientService;
import com.primelife.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class KeyFetchServiceImpl implements KeyFetchService {

    @Autowired
    SecretClientService secretClientService;

    @Value("${spring.datasource.username}")
    String dbUsernameName;

    @Value("${spring.datasource.password}")
    String dbPasswordName;


    private static final HashMap<String, String> keyCache = new HashMap<>();

    @Override
    public String fetchDbUsername() throws KeyVaultException {
        log.trace("Enter Method fetchDbUsername");

        if (keyCache.containsKey(dbUsernameName)) {
            log.info("Db username key found in cache");
            return keyCache.get(dbUsernameName);
        }

        SecretClient secretClient = secretClientService.fetchSecretClient();
        try {

            KeyVaultSecret secret = secretClient.getSecret(dbUsernameName);
            String secretValue = secret.getValue();

            keyCache.put(dbUsernameName, secretValue);
            log.info("DB username placed in cache");

            log.info("Exit Method fetchDbUsername: Successfully fetched db username");
            return secretValue;
        }
        catch (Exception e) {
            log.error("Exit Method fetchDbUsername: an error occurred when trying to fetch db username");
            throw new KeyVaultException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    @Override
    public String fetchDbPassword() throws KeyVaultException {
        log.trace("Enter Method fetchDbPassword");

        if (keyCache.containsKey(dbPasswordName)) {
            log.info("Db password found in cache");
            return keyCache.get(dbPasswordName);
        }

        SecretClient secretClient = secretClientService.fetchSecretClient();
        try {

            KeyVaultSecret secret = secretClient.getSecret(dbPasswordName);
            String secretValue = secret.getValue();

            keyCache.put(dbPasswordName, secretValue);
            log.info("DB password placed in cache");

            log.info("Exit Method fetchDbPassword: Successfully fetched db password");
            return secretValue;
        }
        catch (Exception e) {
            log.error("Exit Method fetchDbPassword: an error occurred when trying to get db password");
            throw new KeyVaultException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }
}
