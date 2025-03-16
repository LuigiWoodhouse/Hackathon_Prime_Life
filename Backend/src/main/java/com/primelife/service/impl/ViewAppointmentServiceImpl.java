package com.primelife.service.impl;

import com.primelife.entity.Appointment;
import com.primelife.repository.AppointmentRepository;
import com.primelife.response.Constants;
import com.primelife.response.GenericResponse;
import com.primelife.service.LoginService;
import com.primelife.service.ViewAppointmentService;
import com.primelife.utils.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ViewAppointmentServiceImpl implements ViewAppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    LoginService loginService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public GenericResponse viewAppointmentsByPatientId(String patientIdInRequest) throws Exception {
        log.trace("Enter method viewAppointment param: {} ",patientIdInRequest);

        String patientId = loginService.authenticateUser(httpServletRequest);

        if (patientIdInRequest.equals(patientId)) {
            log.info("IDs matches");
        }
        else {
            log.error("patient ID {} in request does not match patient ID in token {}", patientIdInRequest,patientId);
            throw new NoPermissionException("not allowed ");
        }


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
    public GenericResponse viewAllAppointments() throws Exception {
        log.trace("Enter method viewAllAppointments ");
        GenericResponse result = new GenericResponse();

        String accessToken = loginService.parseAccessToken(httpServletRequest);
        Map<String, String> userDetails = loginService.fetchUserDetailsFromAccessToken(accessToken);
        String role = userDetails.get("role");

        if (!role.equals(Role.DOCTOR.name()))  {
            log.error("role is not allowed");
            throw new NoPermissionException(Constants.NOT_ALLOWED);
        }

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
    public Appointment findByAppointmentByAppointment(Integer appointmentId) throws Exception {
        log.trace("Enter method viewAllAppointments ");


        Appointment appointment = new Appointment();
        try{
            appointment = appointmentRepository.findAppointmentByAppointmentId(appointmentId);
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
