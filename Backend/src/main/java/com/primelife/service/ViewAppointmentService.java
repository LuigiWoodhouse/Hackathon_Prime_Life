package com.primelife.service;

import com.primelife.entity.Appointments;
import com.primelife.response.GenericResponse;

import java.util.ArrayList;

public interface ViewAppointmentService {
    public GenericResponse viewAppointment(Integer id);
    public GenericResponse viewAllAppointments();
}
