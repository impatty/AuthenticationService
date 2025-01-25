package org.authenticationservice.authenticationservice.Service;

import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Exceptions.InvalidUserORPasswordException;
import org.authenticationservice.authenticationservice.Exceptions.UserAlreadyExistsException;
import org.authenticationservice.authenticationservice.Exceptions.UserNotFoundException;
import org.authenticationservice.authenticationservice.Models.User;
import org.authenticationservice.authenticationservice.Repository.AuthRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class AuthService implements IAuthService {
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public AuthService(AuthRepository authRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authRepository = authRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public UserDTO signup(String email, String password) throws UserAlreadyExistsException {
        // check if user is already signed up
        Optional<User> userOptional = authRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new UserAlreadyExistsException("User already exists! Please try logging...");
        }
        // if new user, create User and persist in DB
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user = authRepository.save(user);
        //UserDTO userDTO = new UserDTO();
        return from(user);
    }

    @Override
    public UserDTO signin(String email, String password) throws UserNotFoundException,InvalidUserORPasswordException {
        Optional<User> userOptional = authRepository.findByEmail(email);
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("User now found...");
        }

        User user = userOptional.get();
        //String hashedPassword = bCryptPasswordEncoder.encode(password);
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserORPasswordException("Invalid Login credentials...");
        }
        return from(userOptional.get());
    }


    public UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
