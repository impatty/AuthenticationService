package org.authenticationservice.authenticationservice.Controller;

import org.authenticationservice.authenticationservice.DTOs.LoginRequest;
import org.authenticationservice.authenticationservice.DTOs.SignUpRequest;
import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Service.IAuthService;
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
    public UserDTO signup(@RequestBody SignUpRequest signUpRequest) {
        UserDTO userDTO = new UserDTO();
        try {

        }


        return userDTO;
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = new UserDTO();
        return userDTO;
    }
}
