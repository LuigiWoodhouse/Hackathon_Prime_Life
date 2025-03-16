package com.primelife.service;

import com.primelife.request.CreatePatientRequest;

public interface PatientService {

    String createPatient(String patientName, String symptom);
}
