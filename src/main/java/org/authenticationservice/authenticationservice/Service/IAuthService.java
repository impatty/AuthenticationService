package org.authenticationservice.authenticationservice.Service;

import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Exceptions.InvalidUserORPasswordException;
import org.authenticationservice.authenticationservice.Exceptions.UserAlreadyExistsException;
import org.authenticationservice.authenticationservice.Exceptions.UserNotFoundException;
import org.springframework.data.util.Pair;

public interface IAuthService {
    public UserDTO signup(String email, String password) throws UserAlreadyExistsException;
    public Pair<UserDTO, String> signin(String email, String password) throws UserNotFoundException, InvalidUserORPasswordException;
    //public UserDT
    //public UserDTO
}
