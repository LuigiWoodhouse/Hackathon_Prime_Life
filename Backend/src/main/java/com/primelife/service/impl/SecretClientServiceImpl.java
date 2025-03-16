package com.primelife.service.impl;

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.primelife.exception.KeyVaultException;
import com.primelife.service.SecretClientService;
import com.primelife.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecretClientServiceImpl implements SecretClientService {

    SecretClient secretClient;

    @Value("${key.vault.url}")
    String keyVaultUrl;

    @Override
    public SecretClient fetchSecretClient() throws KeyVaultException {
        log.trace("Enter Method fetchSecretClient");

        try{
            secretClient = new SecretClientBuilder()
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .vaultUrl(keyVaultUrl)
                    .httpLogOptions( new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS))
                    .buildClient();

            log.info("Exit Method fetchSecretClient: Successfully fetched SecretClient");
            return secretClient;
        }
        catch(Exception e ) {
            log.error("Exit Method fetchSecretClient: an error occurred when trying to get SecretClient");
            throw new KeyVaultException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());

        }
    }
}