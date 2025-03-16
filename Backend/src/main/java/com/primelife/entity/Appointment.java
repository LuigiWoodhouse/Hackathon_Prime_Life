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

    @NotBlank
    private String patientName;

    @NotBlank
    private String visitReason;

    @CreationTimestamp
    private Date dateCreated;

    @NotBlank
    private String appointmentDate;

    @NotBlank
    private String patientId;

    @NotBlank
    private String doctor;
}