package com.primelife.service.impl;

import com.primelife.entity.Patient;
import com.primelife.entity.Staff;
import com.primelife.repository.PatientRepository;
import com.primelife.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	PatientRepository patientRepository;


	@Autowired
	StaffRepository staffRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Staff staff = staffRepository.findByUsernameIgnoreCase(username);
		if (staff != null) {
			return UserDetailsImpl.buildStaff(staff);
		}


		Patient patient = patientRepository.findByUsernameIgnoreCase(username);
		if (patient == null) {
			throw new UsernameNotFoundException("Could not find patient");
		}
		return UserDetailsImpl.buildPatient(patient);

	}
}