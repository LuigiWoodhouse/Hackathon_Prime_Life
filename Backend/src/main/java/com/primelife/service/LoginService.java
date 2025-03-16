package com.primelife.service;

import com.primelife.exception.TokenException;
import com.primelife.request.LoginRequest;
import com.primelife.response.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface LoginService {
    ResponseEntity<JwtResponse> loginUser(LoginRequest loginRequest) throws Exception;

    String parseAccessToken(HttpServletRequest request) throws TokenException;

    String validateAccessToken(String accessToken) throws Exception;

    Map<String, String> fetchUserDetailsFromAccessToken(String accessToken) throws TokenException;

    String validateUser(HttpServletRequest httpServletRequest) throws Exception;

    String authenticateUser(HttpServletRequest httpServletRequest) throws Exception;
}
