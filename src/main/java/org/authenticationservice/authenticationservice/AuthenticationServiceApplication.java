package org.authenticationservice.authenticationservice;

import org.authenticationservice.authenticationservice.DTOs.SignUpRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        //SignUpRequest signUpRequest = new SignUpRequest();
        //String email = signUpRequest.getEmail();
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }
}
