package com.primelife.utils;

import com.primelife.exception.EmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class EmailValidator {

    public static void validateEmail(String email) throws EmailException {
        log.trace("Enter Method validateEmail:{}", email);

        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3}";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            log.info("Exit Method validateEmail: Validation successful: Email is in good format: {}", email);
        }
        else {
            log.error("Exit Method validateEmail: Validation failed: Email is in bad format: {}", email);
            throw new EmailException(HttpStatus.BAD_REQUEST.value(), ResponseCode.INVALID_EMAIL_FORMAT.getCode());
        }
    }
}
