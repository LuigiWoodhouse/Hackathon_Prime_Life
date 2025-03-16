package com.primelife.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.primelife.entity.Patient;
import com.primelife.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id; // Can be either patientId or userId
    private String username;

    @JsonIgnore
    private String password;

    private String email;
    private boolean isAccountNonLocked;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isEnabled;

    public UserDetailsImpl(String id, String username, String password, String email,
                           Collection<? extends GrantedAuthority> authorities,
                           boolean isEnabled, boolean isAccountNonLocked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.isEnabled = isEnabled;
        this.isAccountNonLocked = isAccountNonLocked;
    }

    public static UserDetailsImpl buildPatient(Patient patient) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(patient.getRole().name()));
        return new UserDetailsImpl(
                patient.getPatientId(),
                patient.getUsername(),
                patient.getPassword(),
                patient.getEmail(),
                authorities,
                patient.isEnabled(),
                patient.isAccountNonLocked()
        );
    }

    public static UserDetailsImpl buildStaff(Staff staff) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(staff.getRole()));
        return new UserDetailsImpl(
                staff.getUserId(),
                staff.getUsername(),
                staff.getPassword(),
                null, // Staff entity does not have an email field
                authorities,
                staff.isEnabled(),
                staff.isAccountNonLocked()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
