package com.primelife.service.impl;

import com.primelife.exception.KeyException;
import com.primelife.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Service
public class KeyCreationServiceImpl implements KeyCreationService {

    @Override
    public PrivateKey generatePrivateKey(String privateKeyStr) throws KeyException {
        log.trace("Enter Method generatePrivateKey");

        try{
            // Decode the Base64 encoded private key string into a byte array.
            byte[] privateBytes = Base64.getDecoder().decode(privateKeyStr);

            // Create a PKCS8EncodedKeySpec object using the decoded byte array.
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);

            // Get an instance of the RSA KeyFactory.
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Generate the private key from the key specification.
            log.info("Return Method generatePrivateKey: Generated private key from the key spec successfully");
            return keyFactory.generatePrivate(keySpec);
        }
        catch (Exception e) {
            log.error("Return Method generatePrivateKey: an error occurred in generating private key from the key spec");
                throw new KeyException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }
    @Override
    public PublicKey generatePublicKey(String publicKeyStr) throws KeyException {
        log.trace("Enter Method generatePublicKey");

        try{
            // Decode the Base64 encoded public key string into a byte array.
            byte[] publicBytes = Base64.getDecoder().decode(publicKeyStr);

            // Create an X509EncodedKeySpec object using the decoded byte array.
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);

            // Get an instance of the RSA KeyFactory.
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Generate the public key from the key specification.
            log.info("Return Method generatePublicKey: Generated public key from the key spec successfully");
            return keyFactory.generatePublic(keySpec);
        }
        catch (Exception e) {
            log.error("Return Method generatePublicKey: an error occurred in generating public key from the key spec");
                throw new KeyException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }
}