package com.primelife.controller;

import com.primelife.request.LoginRequest;
import com.primelife.response.GenericResponse;
import com.primelife.response.JwtResponse;
import com.primelife.service.LoginService;
import com.primelife.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/login" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LoginController {

	@Autowired
	LoginService loginService;

	@PostMapping("/authenticate")
	public ResponseEntity<GenericResponse> signInUser(@Validated @RequestBody LoginRequest loginRequest) throws Exception {

		ResponseEntity <GenericResponse> responseEntity;
		GenericResponse result = new GenericResponse();

		try {

			ResponseEntity<JwtResponse> response = loginService.loginUser(loginRequest);
			result.setStatusCode(200);
			result.setMessage(HttpStatus.OK.getReasonPhrase());
			result.setData(response.getBody());
			responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
			log.info("Return Method signInUser: Status:200(Success)");
		}
		catch (BadCredentialsException e ) {
			result.setStatusCode(400);
			result.setMessage("Invalid credentials");
			responseEntity = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			log.error("Return Method signInUser: Status:400(Bad Request)",e);
		}
		catch (DisabledException e ) {
			result.setStatusCode(423);
			result.setMessage(e.getMessage());
			responseEntity = new ResponseEntity<>(result, HttpStatus.LOCKED);
			log.error("Return Method signInUser: Status:423(Locked)",e);
		}
		catch (Exception e ) {
			result.setStatusCode(500);
			result.setMessage(e.getMessage());
			responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("Return Method signInUser: Status:500(Internal Server Error)",e);
		}
        return responseEntity;
    }
}