package com.primelife.service.impl;

import com.primelife.exception.TokenException;
import com.primelife.request.LoginRequest;
import com.primelife.response.JwtResponse;
import com.primelife.service.KeyCreationService;
import com.primelife.service.KeyFetchService;
import com.primelife.service.LoginService;
import com.primelife.utils.ResponseCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    KeyCreationService keyCreationService;

    @Autowired
    KeyFetchService keyFetchService;


    @Override
    public ResponseEntity<JwtResponse> loginUser(LoginRequest loginRequest) throws Exception {
        log.trace("Enter Method loginUser");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();



            List<String> roles = userDetailsImpl.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            log.info("Extracting user role from UserDetailsImpl object,{}", roles);

            log.info("Exit Method loginUser: login successful");
            return generateTokens( userDetailsImpl);
        }
        catch (AuthenticationException e) {
            log.error("Exit Method loginUser: Authentication failed: {}", e.getMessage());
            throw e;
        }
    }

    private ResponseEntity<JwtResponse> generateTokens( UserDetailsImpl userDetailsImpl) throws Exception {
        log.trace("Enter Method generateTokens");

        String accessToken = createJwt(userDetailsImpl);

        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);

        log.info("Exit Method generateTokens: Access Token generated successfully");
        return ResponseEntity.ok(new JwtResponse(response.getAccessToken()));
    }

    public String createJwt(UserDetailsImpl user) throws Exception {
        log.trace("Enter Method createJwt");

        PrivateKey rsaPrivateKey = keyCreationService.generatePrivateKey(keyFetchService.fetchJwtPrivateKey());
        try{
            String jwt = Jwts.builder()
                    .setHeaderParam("typ","JWT")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + (long) 43800 * 60 * 1000)) // 1 month
                    .signWith(SignatureAlgorithm.RS256, rsaPrivateKey)
                    .claim("patientId", user.getPatientId())
                    .claim("role", user.getAuthorities())
                    .claim("iss","website")
                    .compact();

            log.info("Return Method createJwt: Token generated successfully :\n{}", jwt);
            return jwt;
        }
        catch (Exception e) {
            log.error("Exit Method createJwt: Failed to generate jwt: an error occurred in generating jwt",e);
            throw new TokenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }
}
