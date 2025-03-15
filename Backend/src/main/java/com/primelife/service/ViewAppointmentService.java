package com.primelife.service;

import com.primelife.entity.Appointment;
import com.primelife.response.GenericResponse;

import java.util.ArrayList;

public interface ViewAppointmentService {
    public GenericResponse viewAppointmentsByPatientId(String id);
    public GenericResponse viewAllAppointments();
}
