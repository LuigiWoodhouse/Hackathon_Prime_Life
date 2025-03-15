package com.primelife.request;

import lombok.Data;

@Data
public class BookAppointmentRequest {

    private String patientName;
    private String visitReason;
    private String appointmentDate;
}
