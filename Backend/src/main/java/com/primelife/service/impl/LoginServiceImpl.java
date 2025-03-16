package com.primelife.service.impl;

import com.primelife.exception.TokenException;
import com.primelife.request.LoginRequest;
import com.primelife.response.JwtResponse;
import com.primelife.service.KeyCreationService;
import com.primelife.service.KeyFetchService;
import com.primelife.service.LoginService;
import com.primelife.utils.ResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                    .claim("patientId", user.getId())
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

    @Override
    public String parseAccessToken(HttpServletRequest request) throws TokenException {
        log.trace("Enter Method parseAccessToken");

        try {
            String headerAuth = request.getHeader("Authorization");
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
                if (headerAuth.length() > 7) {  // Ensure the length is sufficient
                    String token = headerAuth.substring(7);
                    log.info("Return Method parseAccessToken: Access Token has been parsed successfully:");
                    return token;
                }
                else {
                    log.warn("Return Method parseAccessToken: Authorization header is too short to contain a valid token.");
                    return null;
                }
            }
            else {
                log.info("JWT not required for this path action. Proceed as normal");
                return null;
            }
        }
        catch (Exception e) {
            log.error("Return Method parseAccessToken: An error occurred while trying to parse the access token", e);
            throw new TokenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    @Override
    public String validateAccessToken(String accessToken) throws Exception {
        log.trace("Enter Method validateAccessToken: {}", accessToken);

        PublicKey rsaPublicKey = keyCreationService.generatePublicKey(keyFetchService.fetchJwtPublicKey());

        try {
            Jwts.parser().setSigningKey(rsaPublicKey).parseClaimsJws(accessToken);

            log.info("Return Method validateAccessToken: Access Token has been validated successfully \n{}", accessToken);
            return accessToken;
        }
        catch (SignatureException e) {
            log.error("Return Method validateAccessToken: Invalid JWT signature: {}", e.getMessage());
            throw e;
        }
        catch (ExpiredJwtException e) {
            log.error("Return Method validateAccessToken: JWT token is expired: {}", e.getMessage());
            throw e;
        }
        catch (IllegalArgumentException e) {
            log.error("Return Method validateAccessToken: JWT claims string is empty: {}", e.getMessage());
            throw e;
        }
        catch (MalformedJwtException e) {
            log.error("Return Method validateAccessToken: Invalid JWT token: {}", e.getMessage());
            throw e;
        }
        catch (UnsupportedJwtException e) {
            log.error("Return Method validateAccessToken: JWT token is unsupported: {}", e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("Return Method validateAccessToken: an error occurred in validating access token", e);
            throw new TokenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    @Override
    public Map<String, String> fetchUserDetailsFromAccessToken(String accessToken) throws TokenException {
        log.trace("Enter Method fetchUserDetailsFromAccessToken: {}", accessToken);

        try {
            // Generate the public key
            PublicKey rsaPublicKey = keyCreationService.generatePublicKey(keyFetchService.fetchJwtPublicKey());

            // Parse the claims from the access token
            Claims claims = Jwts.parser().setSigningKey(rsaPublicKey).parseClaimsJws(accessToken).getBody();


            String patientId = (String)  claims.get("patientId");
            List<Map<String, String>> roles = (List<Map<String, String>>) claims.get("role");
            String role = roles.get(0).get("authority"); // Extract "PATIENT"



            log.info("Return fetchUserDetailsFromAccessToken , patientId={}" , patientId);

            // Return a map of user details
            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("patientId", patientId);
            userDetails.put("role", role);

            return userDetails;
        }
        catch (Exception e) {
            log.error("Return fetchUserDetailsFromAccessToken: an error occurred in fetching name from the accessToken", e);
            throw new TokenException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseCode.OPERATION_FAILED.getCode());
        }
    }

    @Override
    public String validateUser(HttpServletRequest httpServletRequest) throws Exception {
        log.trace("Enter Method validateUser");
        String parsedAccessToken = parseAccessToken(httpServletRequest);
        String validatedAccessToken = validateAccessToken(parsedAccessToken);
        log.info("Exit Method validateUser: user validated successfully");
        return validatedAccessToken;
    }

    @Override
    public String authenticateUser(HttpServletRequest httpServletRequest) throws Exception {
        log.trace("Enter Method authenticateUser");
        String accessToken = validateUser(httpServletRequest);
        Map<String, String> userDetails = fetchUserDetailsFromAccessToken(accessToken);

        String patientId = userDetails.get("patientId");

        log.trace("Exit Method authenticateUser: user authenticated successfully");
        return patientId;
    }
}
