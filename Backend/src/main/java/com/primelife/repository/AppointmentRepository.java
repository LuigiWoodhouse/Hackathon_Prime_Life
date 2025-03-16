package com.primelife.repository;

import com.primelife.entity.Appointment;
import com.primelife.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findAllByPatientId(String id);
    List<Appointment> findAll();
    Appointment findAppointmentByAppointmentId(Integer id);
<<<<<<< HEAD
    List<Appointment> findAllByOrderByAppointmentDateAsc();
=======

    Appointment findByAppointmentId(Integer appointmentId);


>>>>>>> a15c5c766b9826ab20cd8f039599d14b0d84d29c
}
