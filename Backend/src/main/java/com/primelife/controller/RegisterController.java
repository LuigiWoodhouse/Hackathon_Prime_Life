package com.primelife.controller;

import com.primelife.exception.EmailException;
import com.primelife.exception.RegisterException;
import com.primelife.exception.TokenException;
import com.primelife.request.RegisterRequest;
import com.primelife.response.GenericResponse;
import com.primelife.service.RegisterService;
import com.primelife.utils.LinkUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/register" , produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RegisterController {

    @Autowired
    RegisterService registerService;


    @PostMapping("/new/user")
    public ResponseEntity<GenericResponse> registerNewUser(
            @RequestBody RegisterRequest registerRequest,
            HttpServletRequest request

    )
    {
        log.trace("Enter Method registerNewUser");

        ResponseEntity<GenericResponse> responseEntity;
        GenericResponse result = new GenericResponse();

        try {
            registerService.registerNewUser(registerRequest, LinkUtility.getSiteURL(request));
            result.setStatusCode(200);
            result.setMessage(HttpStatus.OK.getReasonPhrase());
            responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
			log.info("Return Method registerNewUser: Status:200(Success)");
        }
        catch (EmailException | RegisterException e) {
            result.setStatusCode(400);
            result.setMessage(e.getMessage());
            responseEntity = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            log.error("Return Method registerNewUser: Status:400(Bad Request) ", e);
        }
        catch (Exception e) {
            result.setStatusCode(500);
            result.setMessage(e.getMessage());
            responseEntity = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            log.error("Return Method registerNewUser: Status:500(Internal Server Error) ", e);
        }
        return responseEntity;
    }

    @GetMapping("/new/verify")
    public String verifyUser(@RequestParam("code") String code, Model model) {
        log.trace("Enter Method verifyUser: {}", code);

        try {
            registerService.verifyNewUser(code);
            log.info("Exit Method verifyUser: User verified successfully");
            model.addAttribute("message", "User verified successfully!");
            return "verify"; // This will load verify.html
        }
        catch (EmailException e) {
            log.error("Exit Method verifyUser: User verified already : Bad Request(400)");
            model.addAttribute("message", "User already verified.");
        }
        catch (TokenException e) {
            log.error("Exit Method verifyUser: Verification Token expired or invalid : Bad Request(400)");
            model.addAttribute("message", "Verification token expired or invalid.");
        }
        catch (Exception e) {
            log.error("Exit Method verifyUser: Failed to verify user : Internal Server Error(500)");
            model.addAttribute("message", "Internal Server Error. Please try again later.");
        }
        return "verify";
    }
}