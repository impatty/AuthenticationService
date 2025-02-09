package org.authenticationservice.authenticationservice.Exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class UserSessionNotFound extends Exception {
    public UserSessionNotFound(String message) {
        super(message);
    }
}
