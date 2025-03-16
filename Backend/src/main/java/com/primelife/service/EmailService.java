package com.primelife.service;


import com.primelife.entity.Patient;
import com.primelife.exception.EmailException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public interface EmailService {


    void sendVerificationEmail(Patient patient, String siteURL) throws EmailException;
}
