package com.primelife.service;

import com.primelife.exception.AppointmentException;
import com.primelife.request.BookAppointmentRequest;

public interface BookAppointmentService {
    void bookAppointment(BookAppointmentRequest bookAppointmentRequest) throws AppointmentException;
}
