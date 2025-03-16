package com.primelife.authentication;
import com.primelife.authentication.bean.AmazonCognito;
import com.primelife.authentication.bean.Userdetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class ValidateJWTToken {

    @Autowired
    AmazonCognito amazonCognito;

    public  boolean validateToken(String accessToken) {
        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(amazonCognito.getRegion()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            GetUserRequest request = GetUserRequest.builder()
                    .accessToken(accessToken)
                    .build();

            GetUserResponse response = cognitoClient.getUser(request);
            System.out.println("Token is valid for user: " + response.username());
            return true;

        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String validateAndReturnToken(String accessToken) {

      log.trace("Enter method validateAndReturnToken ");
        try{
            boolean validateToken =  validateTokenV2(accessToken);

            if(validateToken) {
             return accessToken;
         }

        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());

        }
        log.trace("Return method validateAndReturnToken ");

        return null;
    }


    public Userdetails getUserDetailsFromToken(String accessToken) {
        log.trace("Enter method getUserDetails ");

        Userdetails userdetails = new Userdetails();
        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(amazonCognito.region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            GetUserRequest request = GetUserRequest.builder()
                    .accessToken(accessToken)
                    .build();

            GetUserResponse response = cognitoClient.getUser(request);

            // Extract user details (example: username and email)
            String username = response.userAttributes().stream()
                    .filter(attr -> attr.name().equals("User name"))
                    .map(attr -> attr.value())
                    .findFirst()
                    .orElse("No user found");


            String email = response.userAttributes().stream()
                    .filter(attr -> attr.name().equals("Email Address"))
                    .map(attr -> attr.value())
                    .findFirst()
                    .orElse("No email found");

            String phoneNumber = response.userAttributes().stream()
                    .filter(attr -> attr.name().equals("Phone number"))
                    .map(attr -> attr.value())
                    .findFirst()
                    .orElse("No email found");

            String userId = response.userAttributes().stream()
                    .filter(attr -> attr.name().equals("User ID"))
                    .map(attr -> attr.value())
                    .findFirst()
                    .orElse("No email found");

            userdetails.setEmail(username);
            userdetails.setPhoneNumber(phoneNumber);
            userdetails.setEmail(email);


        } catch (Exception e) {
           log.error("Failed to fetch user details: " + e.getMessage());
        }
        log.trace("Return method getUserDetails {}", userdetails);
        return userdetails;

    }

    public  boolean validateTokenV2(String token) {
        try {
            String jwksUrl = String.format(amazonCognito.getUrl(),
                    amazonCognito.getRegion(),amazonCognito.getPoolId() );

            DecodedJWT decodedJWT = JWT.decode(token);
            JwkProvider provider = new JwkProviderBuilder(new URL(jwksUrl))
                    .cached(10, 24, TimeUnit.HOURS)
                    .build();

            Jwk jwk = provider.get(decodedJWT.getKeyId());
            RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();

            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(String.format("https://cognito-idp.%s.amazonaws.com/%s", amazonCognito.getRegion(),amazonCognito.getPoolId()))
                    .build();

            verifier.verify(token); // If verification fails, an exception is thrown
            return true; // Token is valid
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }


}
