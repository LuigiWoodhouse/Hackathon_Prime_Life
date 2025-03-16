package com.primelife.service;

import com.primelife.request.CancelAppointmentRequest;
import com.primelife.request.UpdateAppointmentRequest;

public interface ModifyAppointmentService {

    void modifyAppointment(UpdateAppointmentRequest updateAppointmentRequest, Integer AppointmentId);
    void cancelAppointment(Integer appointmentId, String patientId, CancelAppointmentRequest cancelAppointmentRequest);
}
