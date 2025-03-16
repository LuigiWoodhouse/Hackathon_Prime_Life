package com.primelife.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;


@Data
@Entity
public class Staff {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String userId;

    private String name;

    @CreationTimestamp
    private Date dateCreated;

    private String role;

    @NotBlank
    @JsonIgnore
    private String username;

    @NotBlank
    @JsonIgnore
    private String password;

    private boolean isEnabled = true;

    private boolean isAccountNonLocked = true;
}