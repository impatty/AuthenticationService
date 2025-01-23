package org.authenticationservice.authenticationservice.Service;

import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Exceptions.UserAlreadyExistsException;

public interface IAuthService {
    public UserDTO signup(String email, String password) throws UserAlreadyExistsException;
    public UserDTO signip(String email, String password);
}
