package com.primelife.repository;

import com.primelife.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {


    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    Patient findByUsernameIgnoreCase(String username);

    Patient findByEmailIgnoreCase(String email);

    Patient findByVerificationToken(String verificationToken);
}
