package com.primelife.service;

import com.primelife.entity.Appointments;

import java.util.ArrayList;

public interface ViewAppointmentService {
    public Appointments viewAppointment(long id);
    public ArrayList<Appointments> viewAllAppointments();
}
