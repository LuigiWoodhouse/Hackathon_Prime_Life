package com.primelife.service.impl;

import com.primelife.entity.Appointment;
import com.primelife.repository.AppointmentRepository;
import com.primelife.request.CancelAppointmentRequest;
import com.primelife.request.UpdateAppointmentRequest;
import com.primelife.service.ModifyAppointmentService;
import com.primelife.service.ViewAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class ModifyAppointmentServiceImpl implements ModifyAppointmentService {
    @Autowired
    ViewAppointmentService viewAppointmentService;
    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public void modifyAppointment(UpdateAppointmentRequest updateAppointmentRequest, Integer AppointmentId) {
        log.trace("Enter method  modifyAppointment ");
       try {
             Appointment excistingAppointment = viewAppointmentService.findByAppointmentByAppointment(AppointmentId);
            if (excistingAppointment != null) {

                excistingAppointment.setAppointmentId(updateAppointmentRequest.getAppointmentId());
                excistingAppointment.setAppointmentDate(updateAppointmentRequest.getAppointmentDate());
                excistingAppointment.setVisitReason(updateAppointmentRequest.getVisitReason());
                excistingAppointment.setDateCreated(updateAppointmentRequest.getDateCreated());
                excistingAppointment.setPatientName(updateAppointmentRequest.getPatientName());
                appointmentRepository.save(excistingAppointment);

            }
        } catch (Exception e) {
            log.error("Error occured in modifyAppointment");
        }
        log.trace("Return method  modifyAppointment ");

    }

    @Override
    public void cancelAppointment(Integer appointmentId, String patientId, CancelAppointmentRequest cancelAppointmentRequest) {

        log.trace("Enter method  modifyAppointment ");
        try {
            Appointment excistingAppointment = viewAppointmentService.findByAppointmentByAppointment(appointmentId);
            if (excistingAppointment != null) {

                excistingAppointment.setDateCancelled(new Date());
                excistingAppointment.setCancellationReason(cancelAppointmentRequest.getCancellationReason());
                excistingAppointment.setAppointmentStatus("CANCELLED");
                appointmentRepository.save(excistingAppointment);

            }
        } catch (Exception e) {
            log.error("Error occured in modifyAppointment");
        }
        log.trace("Return method  modifyAppointment ");

    }
}
