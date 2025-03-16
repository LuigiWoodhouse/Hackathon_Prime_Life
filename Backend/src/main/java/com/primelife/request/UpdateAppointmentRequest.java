package com.primelife.request;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
public class UpdateAppointmentRequest {

    private Integer appointmentId;


    private String patientName;


    private String visitReason;

    private Date dateCreated;

    private String appointmentDate;

}
