package org.authenticationservice.authenticationservice.Exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
