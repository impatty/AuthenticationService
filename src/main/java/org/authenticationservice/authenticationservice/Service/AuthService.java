package org.authenticationservice.authenticationservice.Service;

import com.nimbusds.jwt.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Exceptions.InvalidUserORPasswordException;
import org.authenticationservice.authenticationservice.Exceptions.UserAlreadyExistsException;
import org.authenticationservice.authenticationservice.Exceptions.UserNotFoundException;
import org.authenticationservice.authenticationservice.Models.User;
import org.authenticationservice.authenticationservice.Repository.AuthRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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
    public Pair<UserDTO, String> signin(String email, String password) throws UserNotFoundException,InvalidUserORPasswordException {
        Optional<User> userOptional = authRepository.findByEmail(email);
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("User now found...");
        }
        User user = userOptional.get();
        //String hashedPassword = bCryptPasswordEncoder.encode(password);
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserORPasswordException("Invalid Login credentials...");
        }
        UserDTO userDTO = from(user);
        //Generating JWT
        String message = """
                {
                   "email": "anurag@gmail.com",
                   "roles": [
                      "instructor",
                      "buddy"
                   ],
                   "expirationDate": "2ndApril2025"
                }""";
        Map<String, String> payload = new HashMap<>();
        payload.put("email", user.getEmail());
        payload.put("createdAt", String.valueOf(System.currentTimeMillis()));
        payload.put("expiresOn", String.valueOf(System.currentTimeMillis() + (1000 * 60 * 60 * 24)));
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

        return Pair.of(userDTO, token);
    }


    public UserDTO from(User user) {
        //HttpHeaders.
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
