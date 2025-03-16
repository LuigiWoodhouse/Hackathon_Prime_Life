package com.primelife.utils;

import com.primelife.entity.Staff;
import com.primelife.repository.StaffRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapAdminInitializer {

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${bootstrap.doctor.username}")
    String adminUsername;

    @Value("${bootstrap.doctor.password}")
    String adminPassword;


    @PostConstruct
    public void initialize() {
        if (staffRepository.count() == 0) {
            Staff staff1 = new Staff();
            staff1.setName("Luigi");
            staff1.setUsername(adminUsername);
            staff1.setPassword(passwordEncoder.encode(adminPassword));
            staff1.setRole(Role.DOCTOR.name());

            staffRepository.save(staff1);

            System.out.println("Bootstrap doctor user created staff" + staff1);
        }
    }
}

