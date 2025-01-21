package org.authenticationservice.authenticationservice.Service;

import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Exceptions.UserAlreadyExistsException;
import org.authenticationservice.authenticationservice.Models.User;
import org.authenticationservice.authenticationservice.Repository.AuthRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class AuthService implements IAuthService {
    private final AuthRepository authRepository;
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
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
        user.setPassword(password);
        user = authRepository.save(user);
        //UserDTO userDTO = new UserDTO();
        return from(user);
    }


    public UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    @Override
    public UserDTO login(String email, String password) {
        return null;
    }
}