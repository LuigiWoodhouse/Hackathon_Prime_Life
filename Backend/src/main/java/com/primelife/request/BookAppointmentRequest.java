package com.primelife.request;

import lombok.Data;

@Data
public class BookAppointmentRequest {

    private String visitReason;
    private String appointmentDate;
    private String appointmentTime;
    private String doctor;
    private String symptom;
}
