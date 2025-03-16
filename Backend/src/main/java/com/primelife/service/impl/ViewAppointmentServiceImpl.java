package com.primelife.service.impl;

import com.primelife.entity.Appointment;
import com.primelife.repository.AppointmentRepository;
import com.primelife.response.GenericResponse;
import com.primelife.service.ViewAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ViewAppointmentServiceImpl implements ViewAppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public GenericResponse viewAppointmentsByPatientId(String patientId) {
        log.trace("Enter method viewAppointment param: {} ",patientId);
        List<Appointment> appointmentsArrayList;
        GenericResponse result = new GenericResponse();
        try{
            appointmentsArrayList = appointmentRepository.findAllByPatientId(patientId);
            if(appointmentsArrayList.isEmpty() || appointmentsArrayList == null){
                result.setData(appointmentsArrayList);
                result.setStatusCode(404);
                result.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            }else {
                result.setData(appointmentsArrayList);
                result.setStatusCode(200);
                result.setMessage(HttpStatus.OK.getReasonPhrase());
            }

        }catch (Exception e){
            log.error("Exception occured at method viewAppointment {} ", e.toString());
        }
        log.trace("Return method viewAppointment param: {} ",result);

        return result;
    }

    @Override
    public GenericResponse viewAllAppointments()
    {
        log.trace("Enter method viewAllAppointments ");
        GenericResponse result = new GenericResponse();

        List<Appointment> appointmentsArrayList = new ArrayList<>();
        try{
            appointmentsArrayList = appointmentRepository.findAll();
            if(appointmentsArrayList.isEmpty()){
                result.setData(appointmentsArrayList);
                result.setStatusCode(404);
                result.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            }else {
                result.setData(appointmentsArrayList);
                result.setStatusCode(200);
                result.setMessage(HttpStatus.OK.getReasonPhrase());
            }
        }catch (Exception e){
            log.error("Exception occured in method viewAllAppointments ", e.toString());
        }

        log.trace("Return method viewAllAppointments {}" , result);
       return result;
    }

    @Override
    public Appointment findByAppointmentByAppointment(Integer appointmentId) {
        log.trace("Enter method viewAllAppointments ");


        Appointment appointment = new Appointment();
        try{
            appointment = appointmentRepository.findByAppointmentByAppointmentId(appointmentId);
            if(appointment != null) {
                log.trace("Return method findByAppointmentByAppoint {}" , appointment);
                return appointment;
            }
        }catch (Exception e){
            log.error("Exception occured in method findByAppointmentByAppoint ", e.toString());
        }

        return  appointment;
        }
}
