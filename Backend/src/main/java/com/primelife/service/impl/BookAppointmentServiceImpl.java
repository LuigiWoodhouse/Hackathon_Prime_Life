package com.primelife.service.impl;

import com.primelife.entity.Appointment;
import com.primelife.exception.AppointmentException;
import com.primelife.repository.AppointmentRepository;
import com.primelife.request.BookAppointmentRequest;
import com.primelife.service.BookAppointmentService;
import com.primelife.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookAppointmentServiceImpl implements BookAppointmentService {


    @Autowired
    AppointmentRepository appointmentRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public BookAppointmentServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void bookAppointment(BookAppointmentRequest bookAppointmentRequest) throws AppointmentException {
        log.trace("Enter Method bookAppointment :{} ", bookAppointmentRequest);

        Appointment appointment = new Appointment();

        try {
            appointment.setPatientName(bookAppointmentRequest.getPatientName());
            appointment.setVisitReason(bookAppointmentRequest.getVisitReason());
            appointment.setAppointmentDate(bookAppointmentRequest.getAppointmentDate());

            Appointment  savedAppointment =  appointmentRepository.save(appointment);

            messagingTemplate.convertAndSend("/topic/appointments", savedAppointment);

            log.info("Exit Method bookAppointment: appointment booked successfully");
        }

        catch(Exception e) {
            log.error("Exit Method bookAppointment: an error occurred when booking appointment :{} {} ", e.getMessage(),e.getCause());
            throw new AppointmentException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }
}
