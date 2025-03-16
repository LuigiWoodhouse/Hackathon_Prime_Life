package com.primelife.service.impl;

import com.primelife.entity.Patient;
import com.primelife.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional //This annotation will be needed when JWT is used to authenticate the user
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	PatientRepository patientRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Patient patient = patientRepository.findByUsernameIgnoreCase(username);
		if (patient == null) {
			throw new UsernameNotFoundException("Could not find patient");
		}
		return UserDetailsImpl.build(patient);
	}
}