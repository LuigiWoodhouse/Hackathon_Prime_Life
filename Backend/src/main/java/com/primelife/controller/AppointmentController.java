package com.primelife.controller;


import com.primelife.entity.Appointment;
import com.primelife.request.BookAppointmentRequest;
import com.primelife.request.CancelAppointmentRequest;
import com.primelife.request.UpdateAppointmentRequest;
import com.primelife.response.GenericResponse;
import com.primelife.service.BookAppointmentService;
import com.primelife.service.ModifyAppointmentService;
import com.primelife.service.ViewAppointmentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Book API", description = "Manages Appointment booking for a patient")
public class AppointmentController {


    @Autowired
    BookAppointmentService bookAppointmentService;

    @Autowired
    ViewAppointmentService viewAppointmentService;

    @Autowired
    ModifyAppointmentService modifyAppointmentService;

    @PostMapping("/book")
    @Operation(summary = "book a appointment for patient", description = "Patient is able to book appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appoint book success. no data returned"),
            @ApiResponse(responseCode = "500", description = "Failed to book appointment"),
    })
    public ResponseEntity<GenericResponse>  createAppointment(@RequestBody BookAppointmentRequest bookAppointmentRequest) {

        ResponseEntity <GenericResponse> responseEntity;
        GenericResponse result = new GenericResponse();

        try{
            bookAppointmentService.bookAppointment(bookAppointmentRequest);
            result.setStatusCode(204);
            result.setMessage(HttpStatus.NO_CONTENT.getReasonPhrase());
            responseEntity = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
        catch (Exception e ) {
            result.setStatusCode(500);
            result.setMessage(e.getMessage());
            responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/view/all")
    @Operation(summary = "view all appointment", description = "All appointment are able to be viewed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "VIEW all appointment success."),
            @ApiResponse(responseCode = "500", description = "Failed to view appointment"),
    })
    public ResponseEntity<GenericResponse>  viewAllAppointment(){
        ResponseEntity <GenericResponse> responseEntity;

        GenericResponse result = new GenericResponse();
        try{

            result = viewAppointmentService.viewAllAppointments();

            responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e ) {
            result.setStatusCode(500);
            result.setMessage(e.getMessage());
            responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @GetMapping("/view/patient/{id}")
    @Operation(summary = "view the list of patient's appointment", description = "All appointment for a patient is able to be viewed by doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment is  returned"),
            @ApiResponse(responseCode = "404", description = "Appointment is not found"),
            @ApiResponse(responseCode = "500", description = "Failed to view appointment"),
    })
    public ResponseEntity<GenericResponse>  viewAppointment(
            @PathVariable String id){
        ResponseEntity <GenericResponse> responseEntity;

        GenericResponse result = new GenericResponse();
        Appointment appointments = new Appointment();
        try{

            result = viewAppointmentService.viewAppointmentsByPatientId(id);

            responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e ) {
            result.setStatusCode(500);
            result.setMessage(e.getMessage());
            responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PatchMapping("modify/appointment/{appointmentId}")
    public ResponseEntity<GenericResponse> modifyAppointment(@PathVariable Integer appointmentId, @RequestBody UpdateAppointmentRequest updateAppointMentRequest){

        ResponseEntity <GenericResponse> responseEntity = null;

        GenericResponse result = new GenericResponse();


        try{
            modifyAppointmentService.modifyAppointment(updateAppointMentRequest, appointmentId);
           result.setStatusCode(200);
            result.setMessage(HttpStatus.NO_CONTENT.getReasonPhrase());
            responseEntity = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
        catch (Exception e ) {
            result. setStatusCode(500);
            result.setMessage(e.getMessage());
            responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    @PostMapping("/cancel/{appointmentId}/{patientId}")
    public ResponseEntity<GenericResponse> cancelAppointment(
            @PathVariable Integer appointmentId,
            @PathVariable String patientId,
            @RequestBody CancelAppointmentRequest cancelAppointmentRequest
    ){
        ResponseEntity <GenericResponse> responseEntity = null;

        GenericResponse result = new GenericResponse();


        try{
        modifyAppointmentService.cancelAppointment(appointmentId, patientId, cancelAppointmentRequest );

        result.setStatusCode(200);
        result.setMessage(HttpStatus.NO_CONTENT.getReasonPhrase());
        responseEntity = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }
        catch (Exception e ) {
        result. setStatusCode(500);
        result.setMessage(e.getMessage());
        responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
        return responseEntity;
    }


}
