package org.authenticationservice.authenticationservice.Service;

import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Exceptions.InvalidUserORPasswordException;
import org.authenticationservice.authenticationservice.Exceptions.UserAlreadyExistsException;
import org.authenticationservice.authenticationservice.Exceptions.UserNotFoundException;

public interface IAuthService {
    public UserDTO signup(String email, String password) throws UserAlreadyExistsException;
    public UserDTO signin(String email, String password) throws UserNotFoundException, InvalidUserORPasswordException;
}
