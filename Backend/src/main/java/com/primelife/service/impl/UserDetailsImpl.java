package com.primelife.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.primelife.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String patientId;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private boolean isAccountNonLocked;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isEnabled;

    public <T> UserDetailsImpl(String patientId, String username, String password, List<T> ts) {
        authorities = ts.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());

        this.patientId = patientId;
        this.username = username;
        this.password = password;
    }


    public static UserDetailsImpl build(Patient patient) {
        List<GrantedAuthority> authorities = patient.getRole().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
        return new UserDetailsImpl(
                patient.getPatientId(),
                patient.getUsername(),
                patient.getPassword(),
                patient.getEmail(),
                patient.isEnabled(),
                authorities,
                patient.isAccountNonLocked());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPatientId() {
        return patientId;
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
    public void setUsername(String username) {
    }
}