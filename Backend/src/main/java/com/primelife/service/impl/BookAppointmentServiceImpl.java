package com.primelife.service.impl;

import com.primelife.entity.Appointment;
import com.primelife.entity.Patient;
import com.primelife.exception.AppointmentException;
import com.primelife.repository.AppointmentRepository;
import com.primelife.repository.PatientRepository;
import com.primelife.request.BookAppointmentRequest;
import com.primelife.response.Constants;
import com.primelife.service.BookAppointmentService;
import com.primelife.service.EmailService;
import com.primelife.service.LoginService;
import com.primelife.utils.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookAppointmentServiceImpl implements BookAppointmentService {


    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    LoginService loginService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    EmailService emailService;

    @Autowired
    PatientRepository patientRepository;


    @Override
    public void bookAppointment(BookAppointmentRequest bookAppointmentRequest) throws Exception {
        log.trace("Enter Method bookAppointment :{} ", bookAppointmentRequest);

        Appointment appointment = new Appointment();


        String patientId = loginService.authenticateUser(httpServletRequest);

        Patient existingPatient = patientRepository.findByPatientId(patientId);

        try {
            appointment.setPatientName(existingPatient.getName());
            appointment.setVisitReason(bookAppointmentRequest.getVisitReason());
            appointment.setAppointmentDate(bookAppointmentRequest.getAppointmentDate());
            appointment.setAppointmentTime(bookAppointmentRequest.getAppointmentTime());
            appointment.setAppointmentStatus(Constants.APPT_STATUS_PENDING);
            appointment.setDoctor(bookAppointmentRequest.getDoctor());
            appointment.setSymptom(bookAppointmentRequest.getSymptom());
            appointment.setPatientId(patientId);
            appointment.setEmail(existingPatient.getEmail());

            appointmentRepository.save(appointment);
            log.info("Exit Method bookAppointment: appointment booked successfully");

            emailService.sendCreateAppointmentEmailNotification(appointment.getAppointmentId());
        }

        catch(Exception e) {
            log.error("Exit Method bookAppointment: an error occurred when booking appointment :{} {} ", e.getMessage(),e.getCause());
            throw new AppointmentException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }
}
