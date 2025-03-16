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

    @Value("${spring.mail.username.name}")
    String mailUsernameName;

    @Value("${spring.mail.password.name}")
    String mailPasswordName;

    @Value("${jwt.private.key.name}")
    String jwtPrivateKeyName;

    @Value("${jwt.public.key.name}")
    String jwtPublicKeyName;



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

    @Override
    public String fetchEmailUsername() throws KeyVaultException {
        log.trace("Enter Method fetchEmailUsername");

        if (keyCache.containsKey(mailUsernameName)) {
            log.info("SMTP Mail username found in cache");
            return keyCache.get(mailUsernameName);
        }

        SecretClient secretClient = secretClientService.fetchSecretClient();
        try {

            KeyVaultSecret secret = secretClient.getSecret(mailUsernameName);
            String secretValue = secret.getValue();

            keyCache.put(mailUsernameName, secretValue);
            log.info("SMTP Mail username placed in cache");

            log.info("Exit Method fetchEmailUsername: Successfully fetched email username");
            return secretValue;
        }
        catch (Exception e) {
            log.error("Exit Method fetchEmailUsername: an error occurred when trying to fetch email username");
            throw new KeyVaultException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    @Override
    public String fetchEmailPassword() throws KeyVaultException {
        log.trace("Enter Method fetchEmailPassword");

        if (keyCache.containsKey(mailPasswordName)) {
            log.info("SMTP Mail password found in cache");
            return keyCache.get(mailPasswordName);
        }

        SecretClient secretClient = secretClientService.fetchSecretClient();
        try {
            KeyVaultSecret secret = secretClient.getSecret(mailPasswordName);
            String secretValue = secret.getValue();

            keyCache.put(mailPasswordName, secretValue);
            log.info("SMTP Mail password placed in cache");

            log.info("Exit Method fetchEmailPassword: Successfully fetched email password");
            return secretValue;
        }
        catch (Exception e) {
            log.error("Exit Method fetchMailPassword: an error occurred when trying to fetch email password");
            throw new KeyVaultException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    @Override
    public String fetchJwtPrivateKey() throws KeyVaultException {
        log.trace("Enter Method fetchPrivateKeyValue");

        if (keyCache.containsKey(jwtPrivateKeyName)) {
            log.info("Jwt private key found in cache");
            return keyCache.get(jwtPrivateKeyName);
        }

        SecretClient secretClient = secretClientService.fetchSecretClient();
        try {

            KeyVaultSecret secret = secretClient.getSecret(jwtPrivateKeyName);
            String secretValue = secret.getValue();
            keyCache.put(jwtPrivateKeyName, secretValue);
            log.info("Jwt private key placed in cache");

            log.info("Exit Method fetchPrivateKeyValue: Successfully fetched private key");
            return secretValue;
        }
        catch (Exception e) {
            log.error("Exit Method fetchPrivateKeyValue: an error occurred when trying to fetch private key");
            throw new KeyVaultException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    @Override
    public String fetchJwtPublicKey() throws KeyVaultException {
        log.trace("Enter Method fetchJwtPublicKey");

        if (keyCache.containsKey(jwtPublicKeyName)) {
            log.info("Jwt public key found in cache");
            return keyCache.get(jwtPublicKeyName);
        }

        SecretClient secretClient = secretClientService.fetchSecretClient();
        try {

            KeyVaultSecret secret = secretClient.getSecret(jwtPublicKeyName);
            String secretValue = secret.getValue();

            keyCache.put(jwtPublicKeyName, secretValue);
            log.info("Jwt public key placed in cache");

            log.info("Exit Method fetchJwtPublicKey: Successfully fetched public key");
            return secretValue;
        }
        catch (Exception e) {
            log.error("Exit Method fetchJwtPublicKey: an error occurred when trying to fetch public key");
            throw new KeyVaultException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

}
