package org.authenticationservice.authenticationservice.Controller;

import org.authenticationservice.authenticationservice.DTOs.LoginRequest;
import org.authenticationservice.authenticationservice.DTOs.SignUpRequest;
import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.DTOs.ValidateTokenRequest;
import org.authenticationservice.authenticationservice.Exceptions.*;
import org.authenticationservice.authenticationservice.Repository.UserSessionRepository;
import org.authenticationservice.authenticationservice.Service.IAuthService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserSessionRepository userSessionRepository;
    private IAuthService authService;

    AuthController(IAuthService authService, UserSessionRepository userSessionRepository) {
        this.authService = authService;
        this.userSessionRepository = userSessionRepository;
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
            Pair<UserDTO, String> response = authService.signin(loginRequest.getEmail(), loginRequest.getPassword());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, response.getSecond());
//            headers.add(HttpHeaders.SET_COOKIE, "extra value");
//            System.out.println(headers);
            return new ResponseEntity<UserDTO>(userDTO, headers, HttpStatus.ACCEPTED);
        }catch(UserNotFoundException e) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        } catch (InvalidUserORPasswordException e) {
            return new ResponseEntity<UserDTO>(HttpStatus.UNAUTHORIZED);
        }
    }


    // ValidateToken
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody ValidateTokenRequest validateTokenRequest){
        //fetch the tokens for the userID
        try {
            Boolean isValidToken = authService.validateToken(validateTokenRequest.getUserId(), validateTokenRequest.getToken());
            if(isValidToken)
                return new ResponseEntity<Boolean>(HttpStatus.ACCEPTED);
            else
                throw new InvalidSession("Session is invalid, please login again");
        } catch(UserSessionNotFound userSessionNotFound) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidSession invalidSession) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
