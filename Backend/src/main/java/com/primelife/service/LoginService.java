package com.primelife.service;

import com.primelife.request.LoginRequest;
import com.primelife.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<JwtResponse> loginUser(LoginRequest loginRequest) throws Exception;
}
