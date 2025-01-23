package org.authenticationservice.authenticationservice.Exceptions;

public class InvalidUserORPasswordException extends Exception {
    public InvalidUserORPasswordException(String message) {
        super(message);
    }
}
