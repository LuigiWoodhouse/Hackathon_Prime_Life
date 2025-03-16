package com.primelife.request;


import lombok.Data;

@Data
public class CreatePatientRequest {

    private String name;
    private String email;
    private String symptom;
}
