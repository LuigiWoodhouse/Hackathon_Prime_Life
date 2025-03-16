package com.primelife.service.impl;

import com.primelife.exception.EmailException;
import com.primelife.response.Constants;
import com.primelife.service.EmailService;
import com.primelife.utils.ResponseCode;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender javaMailSender;


    public void sendCreateAppointmentEmailNotification() throws Exception {
        log.trace("Enter Method sendCreateAppointmentEmailNotification :");

        List<String> toAddresses = new ArrayList<>();

        String content =
                "<html>" +
                        "<body>" +
//                        "<p>Hi " + existingPatient.getPatientName + "</p>" +
//                        "<p>"  + Constants.CREATE_APPOINTMENT_MESSAGE +  existingPatient.getAppointmentDate + " " + existingPatient.getAppointmentTime() + "</p>" +
                        "<br>" +
                        "</body>" +
                        "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            for (String to : toAddresses) {
                helper.setTo(to);
                helper.setFrom(Constants.SENDER_EMAIL, Constants.SENDER_NAME);
                helper.setText(content, true);

                javaMailSender.send(message);
            }
            log.info("Return Method sendCreateAppointmentEmailNotification: email alert sent successfully to {}", toAddresses);
        }
        catch (Exception e) {
            log.error("Return Method sendCreateAppointmentEmailNotification: an error occurred while sending email alert to {}: ", toAddresses, e);
            throw new EmailException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }
}
