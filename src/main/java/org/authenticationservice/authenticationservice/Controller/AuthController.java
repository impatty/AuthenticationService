package org.authenticationservice.authenticationservice.Controller;

import org.authenticationservice.authenticationservice.DTOs.LoginRequest;
import org.authenticationservice.authenticationservice.DTOs.SignUpRequest;
import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Exceptions.InvalidUserORPasswordException;
import org.authenticationservice.authenticationservice.Exceptions.UserAlreadyExistsException;
import org.authenticationservice.authenticationservice.Exceptions.UserNotFoundException;
import org.authenticationservice.authenticationservice.Service.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private IAuthService authService;

    AuthController(IAuthService authService) {
        this.authService = authService;
    }

    // signup api
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignUpRequest signUpRequest) {
        UserDTO userDTO = new UserDTO();
        try {
            userDTO = authService.signup(signUpRequest.getEmail(), signUpRequest.getPassword());
            return new ResponseEntity<UserDTO>(HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
        }

        //return userDTO;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserDTO> signin(@RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = new UserDTO();
        try {
            userDTO = authService.signin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.ACCEPTED);
        }catch(UserNotFoundException e) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        } catch (InvalidUserORPasswordException e) {
            return new ResponseEntity<UserDTO>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = new UserDTO();
        return userDTO;
    }
}
