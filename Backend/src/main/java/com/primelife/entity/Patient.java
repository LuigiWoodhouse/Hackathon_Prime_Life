package com.primelife.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name ="patient")
public class Patient {

    @Id
    private String patientId;

    private String name;

    private String username;
    private String password;
    private String email;

    private boolean isEnabled;

    @CreationTimestamp
    private Date userCreationDate;

    private boolean isAccountNonLocked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> role = new HashSet<>();

    @JsonIgnore
    private String verificationToken;
    @CreationTimestamp
    private Date dateCreated;

    private String symptom;


    @JsonIgnore
    private LocalDateTime verificationTokenGenerationTime;

}