package com.primelife.service.impl;

import com.primelife.entity.Appointment;
import com.primelife.entity.Patient;
import com.primelife.exception.EmailException;
import com.primelife.repository.AppointmentRepository;
import com.primelife.repository.PatientRepository;
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

    @Autowired
    AppointmentRepository appointmentRepository;


    @Override
    public void sendCreateAppointmentEmailNotification(Integer appointmentId) throws Exception {
        log.trace("Enter Method sendCreateAppointmentEmailNotification :");

        List<String> toAddresses = new ArrayList<>();

        Appointment existingAppointment = appointmentRepository.findByAppointmentId(appointmentId);


        String content =
                "<html>" +
                        "<body>" +
                        "<p>Hi " + existingAppointment.getPatientName() + "</p>" +
                        "<p>"  + Constants.CREATE_APPOINTMENT_MESSAGE + " " +  existingAppointment.getAppointmentDate() + " " +   " " + existingAppointment.getAppointmentTime() + "</p>" +
                        "<br>" +
                        "</body>" +
                        "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {

                helper.setTo(existingAppointment.getEmail());
                helper.setFrom(Constants.SENDER_EMAIL, Constants.SENDER_NAME);
                helper.setText(content, true);

                javaMailSender.send(message);

            log.info("Return Method sendCreateAppointmentEmailNotification: email alert sent successfully to {}", existingAppointment.getEmail());
        }
        catch (Exception e) {
            log.error("Return Method sendCreateAppointmentEmailNotification: an error occurred while sending email alert to {}: ", toAddresses, e);
            throw new EmailException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }


    @Override
    public void sendVerificationEmail(Patient patient, String siteURL) throws EmailException {
        log.trace("Enter Method sendVerificationEmail");


        String subject = "PetAniTopia - Please verify the registration of your account";
        String verifyURL = siteURL + "/register/new/verify?code=" + patient.getVerificationToken();
        log.info("Return Method sendVerificationEmail: Verification URL generated successfully");

        //creates a new instance of the MimeMessage class using the javaMailSender object.
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {

            String content = String.format(
                    "<html>" +
                            "<head>" +
                            "</head>" +
                            "<body>" +
                            "<p>Hello "  + patient.getName() + "</p>" +
                            "<p>Please follow the instructions below to verify your account on PetAniTopia</p>" +
                            "<p>Press the button below to initiate the account verification process.This must be completed within 24 hrs after receiving this email.</p>" +
                            "<a href=\"" + verifyURL + "\" style=\"text-decoration: none;\">" +
                            "<button style=\"background-color: purple; color: white; padding: 10px; border: none; border-radius: 5px;\">Proceed To Verify My Account</button>" +
                            "</a>" +
                            "</div>" +

                            "</div>" +
                            "</body>" +
                            "</html>");
            //Setting the contents of the email in the object "helper"
            helper.setFrom(Constants.SENDER_EMAIL, Constants.SENDER_NAME);
            helper.setTo(patient.getEmail());
            helper.setSubject(subject);

            log.info("Return Method sendVerificationEmail: Display url successfully {}",verifyURL);
            helper.setText(content, true);

            javaMailSender.send(message);
            log.info("Return Method sendVerificationEmail: Verification Email Was Successfully Sent {}", patient.getEmail());
        }
        catch (Exception e){
            log.error("Return Method sendVerificationEmail: An error occurred while sending verification email to user with email {}: ", patient.getEmail(), e);
            throw new EmailException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }
}
