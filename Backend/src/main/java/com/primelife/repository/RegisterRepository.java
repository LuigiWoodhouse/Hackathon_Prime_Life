package com.primelife.repository;


import com.primelife.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Patient,String> {

    Patient findByVerificationToken(String verificationToken);
    
	boolean existsByEmailIgnoreCase(String email);

	boolean existsByUsernameIgnoreCase(String username);
}