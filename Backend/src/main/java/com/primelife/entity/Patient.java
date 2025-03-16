package com.primelife.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.primelife.utils.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Entity
@Table(name ="patient")
public class Patient {

    @Id
    private String patientId;

    private String name;

    private String username;


    @JsonIgnore
    private String password;
    private String email;

    private boolean isEnabled;

    @CreationTimestamp
    private Date userCreationDate;

    private boolean isAccountNonLocked;

    @Enumerated(EnumType.STRING)
    @Transient
    private Role role = Role.PATIENT;

    @JsonIgnore
    private String verificationToken;
    @CreationTimestamp
    private Date dateCreated;

    private String symptom;

//    private String appointmentDate;
//
//    private String appointmentTime;


    @JsonIgnore
    private LocalDateTime verificationTokenGenerationTime;

}