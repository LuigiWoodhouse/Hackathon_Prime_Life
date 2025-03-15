package com.primelife.service.impl;

import com.primelife.entity.Appointments;
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
    public GenericResponse viewAppointment(Integer id) {
        log.trace("Enter method viewAppointment param: {} ",id);
        Optional<Appointments> appointments = Optional.of(new Appointments());
        GenericResponse result = new GenericResponse();
        try{
            appointments = appointmentRepository.findById(id);
            if(appointments.isEmpty() || appointments == null){
                result.setData(appointments);
                result.setStatusCode(404);
                result.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            }else {
                result.setData(appointments);
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

        List<Appointments> appointmentsArrayList = new ArrayList<>();
        try{
            appointmentsArrayList = appointmentRepository.findAll();
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
            log.error("Exception occured in method viewAllAppointments ", e.toString());
        }

        log.trace("Return method viewAllAppointments {}" , result);
       return result;
    }
}
