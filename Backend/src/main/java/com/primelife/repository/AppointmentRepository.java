package com.primelife.repository;

import com.primelife.entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentRepository extends JpaRepository<Appointments, Integer> {
}
