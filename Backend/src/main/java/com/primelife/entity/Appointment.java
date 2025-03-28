package com.primelife.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name ="appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    private String patientName;

    private String visitReason;

    @CreationTimestamp
    private Date dateCreated;

    private String appointmentDate;

    private String appointmentTime;

    private Date dateCancelled;

    private String appointmentStatus;

    private String  cancellationReason;

    private String patientId;

    private String doctor;

    private String symptom;

    private String email;
}