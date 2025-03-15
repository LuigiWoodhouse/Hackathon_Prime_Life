package com.primelife.service.impl;

import com.primelife.entity.Patient;
import com.primelife.repository.PatientRepository;
import com.primelife.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {


    @Autowired
    PatientRepository patientRepository;

    @Override
    public String createPatient(String patientName, String symptom) {
        log.trace("Enter Method createPatient :{} {}", patientName,symptom);

        Patient newPatient = new Patient();

        newPatient.setName(patientName);
        newPatient.setSymptom(symptom);
        newPatient.setPatientId(UUID.randomUUID().toString());

        patientRepository.save(newPatient);

        String patientId = newPatient.getPatientId();

        log.info("Exit Method createPatient: patient {} has been created ", patientId);
        return patientId;

    }
}
