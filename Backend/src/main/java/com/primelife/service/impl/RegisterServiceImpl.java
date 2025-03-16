package com.primelife.service.impl;

import com.primelife.entity.Patient;
import com.primelife.exception.EmailException;
import com.primelife.exception.RegisterException;
import com.primelife.exception.TokenException;
import com.primelife.repository.PatientRepository;
import com.primelife.request.RegisterRequest;
import com.primelife.service.EmailService;
import com.primelife.service.RegisterService;
import com.primelife.utils.EmailValidator;
import com.primelife.utils.ResponseCode;
import com.primelife.utils.Role;
import com.primelife.utils.StringGenerator;
import com.primelife.utils.TokenUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    PatientRepository patientRepository;

    @Autowired
    EmailService emailService;


    @Override
    public void registerNewUser(RegisterRequest registerRequest, String siteURL) throws RegisterException, EmailException {

        log.trace("Enter Method registerNewUser");


        EmailValidator.validateEmail(registerRequest.getEmail());
        doesUsernameOrEmailAlreadyExist(registerRequest.getUsername(), registerRequest.getEmail());

        try {

            Patient newUser = new Patient();

            newUser.setName(registerRequest.getName());
            newUser.setUsername(registerRequest.getUsername());
            newUser.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
            newUser.setEmail(registerRequest.getEmail());
            newUser.setAccountNonLocked(true);
            String verificationToken = StringGenerator.generateRandomString(12);
            newUser.setVerificationToken(verificationToken);
            newUser.setPatientId(UUID.randomUUID().toString());
            newUser.setRole(Role.PATIENT);

            LocalDateTime currentTime = LocalDateTime.now();
            newUser.setVerificationTokenGenerationTime(currentTime);


            emailService.sendVerificationEmail(newUser, siteURL);

            log.info("Return Method registerNewUser: Patient registered successfully: Username={} , Status={}",registerRequest.getUsername() , HttpStatus.OK);
            patientRepository.save(newUser);
        }
        catch (Exception e) {
            log.error("Return Method registerNewUser: an error occurred in registering user: {}", registerRequest.getUsername(), e);
            throw new RegisterException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    private void doesUsernameOrEmailAlreadyExist(String username, String email) throws RegisterException {
        log.trace("Enter Method doesUsernameOrEmailAlreadyExist:{} {}",username, email);

        if (patientRepository.existsByUsernameIgnoreCase(username)
                || patientRepository.existsByEmailIgnoreCase(email)) {

            log.error("Exit Method doesUsernameOrEmailAlreadyExist: Username or Email Already Exists Username={}, Email={}",
                    username, email);
            throw new RegisterException(HttpStatus.BAD_REQUEST.value(), "unavailable");
        }
        else{
            log.info("Exit Method doesUsernameOrEmailAlreadyExist: Username and Email does not already exist : Username={}, Email={}",
                    username, email);
        }
    }

    private void checkIfVerificationTokenExpired(Patient existingUser) throws TokenException {
        log.trace("Enter Method checkIfVerificationTokenExpired");

        LocalDateTime tokenGenerationTime = existingUser.getVerificationTokenGenerationTime();
        log.info("Fetching verification token generation time:{}", tokenGenerationTime);

        long minutesDifference = TokenUtility.calculateTimeDifference(tokenGenerationTime);
        log.info("Calculating the time difference:...{}", minutesDifference);

        log.info("Checking if verification token is still valid...");
        if (minutesDifference >= 1440) {
            log.error("Return Method updatePassword: Verification token has expired for user: {}", existingUser.getPatientId());
            throw new TokenException(HttpStatus.BAD_REQUEST.value(),"expired");
        }
        else{
            log.info("Return Method checkIfVerificationTokenExpired: Verification Token for user has not expired");
        }
    }


    private void isUserAlreadyVerified(Patient existingUser) throws EmailException {
        log.trace("Enter Method isUserAlreadyVerified:{}", existingUser.getEmail());

        if (existingUser.isEnabled()) {
            log.error("Return Method verifyNewUser: user is already verified: {}", existingUser.getUsername());
            throw new EmailException(HttpStatus.BAD_REQUEST.value() ,"already verified");
        }
        else{
            log.info("Exit Method isUserAlreadyVerified: User not yet verified");
        }
    }

    @Override
    public void verifyNewUser(String verificationToken) throws TokenException, EmailException, RegisterException {
        log.trace("Enter Method verifyNewUser");

        Patient existingUser = findUniqueVerificationToken(verificationToken);
        checkIfVerificationTokenExpired(existingUser);
        isUserAlreadyVerified(existingUser);

        try {
            existingUser.setVerificationToken(null);
            existingUser.setVerificationTokenGenerationTime(null);
            existingUser.setEnabled(true);
            patientRepository.save(existingUser);
            log.info("Enter Method verifyNewUser: user successfully verified: {}", existingUser.getUsername());
        }
        catch (Exception e) {
            log.error("Return Method verifyNewUser: Error occurred in retrieving verification code", e);
            throw new RegisterException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    private Patient findUniqueVerificationToken(String verificationToken) throws RegisterException {
        log.trace("Enter Method findUniqueVerificationToken");

        Patient existingUser = patientRepository.findByVerificationToken(verificationToken);
        log.info("Enter Method findUniqueVerificationToken: Checking if verification token is found... {}", verificationToken);

        if(existingUser == null){
            log.error("Return Method verifyNewUser: Verification token was not found: {}" ,verificationToken);
            throw new RegisterException(HttpStatus.NOT_FOUND.value(),"404");
        }
        else {
            log.info("Exit Method findUniqueVerificationToken: Verification token was found successfully:{}", verificationToken);
            return existingUser;
        }
    }
}
