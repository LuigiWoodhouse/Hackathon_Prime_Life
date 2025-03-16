package com.primelife.repository;


import com.primelife.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Staff findByUsernameIgnoreCase(String username);

}