package com.primelife.controller;


import com.primelife.request.BookAppointmentRequest;
import com.primelife.response.GenericResponse;
import com.primelife.service.BookAppointmentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Book API", description = "Manages Appointment booking for a patient")
public class AppointmentController {


    @Autowired
    BookAppointmentService bookAppointmentService;

    @PostMapping("/book")
    @Operation(summary = "Add email to email alert list for a customer", description = "Assign an email to an existing customer's email alert list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assign email success. no content returned"),
            @ApiResponse(responseCode = "400", description = "Invalid email"),
            @ApiResponse(responseCode = "409", description = "Email already assigned. no duplicate allowed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<GenericResponse>  createAppointment(@RequestBody BookAppointmentRequest bookAppointmentRequest) {

        ResponseEntity <GenericResponse> responseEntity;
        GenericResponse result = new GenericResponse();

        try{
            bookAppointmentService.bookAppointment(bookAppointmentRequest);
            result.setStatusCode(200);
            result.setMessage(HttpStatus.OK.getReasonPhrase());
            responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e ) {
            result.setStatusCode(500);
            result.setMessage(e.getMessage());
            responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
