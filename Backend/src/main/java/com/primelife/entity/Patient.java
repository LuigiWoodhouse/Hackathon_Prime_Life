package com.primelife.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name ="patient")
public class Patient {

    @Id
    private String patientId;

    private String name;

    @CreationTimestamp
    private Date dateCreated;

    private String symptom;

}