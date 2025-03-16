package com.primelife.controller;

import com.primelife.authentication.ValidateJWTToken;
import com.primelife.authentication.bean.LoginRequest;
import com.primelife.response.GenericResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @Autowired
    ValidateJWTToken validateJWTToken;

    @PostMapping("/authenticate")
    public ResponseEntity<GenericResponse>  signInUser(@RequestBody LoginRequest loginRequest) {

        GenericResponse result = new GenericResponse();
        ResponseEntity <GenericResponse> responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

        try {
            boolean validate = validateJWTToken.validateTokenV2(loginRequest.getAccessToken());

            result.setStatusCode(204);
            result.setData(validate);
            result.setMessage(HttpStatus.NO_CONTENT.getReasonPhrase());
            responseEntity = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            result.setStatusCode(500);
            result.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
          log.error("Exception occurred : {} ", e.getMessage());
        }
        log.trace("Return Method SignInUser");
        return responseEntity;

    }
}
