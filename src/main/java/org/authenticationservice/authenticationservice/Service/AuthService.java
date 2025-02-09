package org.authenticationservice.authenticationservice.Service;

import com.nimbusds.jwt.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.authenticationservice.authenticationservice.DTOs.UserDTO;
import org.authenticationservice.authenticationservice.Exceptions.InvalidUserORPasswordException;
import org.authenticationservice.authenticationservice.Exceptions.UserAlreadyExistsException;
import org.authenticationservice.authenticationservice.Exceptions.UserNotFoundException;
import org.authenticationservice.authenticationservice.Exceptions.UserSessionNotFound;
import org.authenticationservice.authenticationservice.Models.Enum.Status;
import org.authenticationservice.authenticationservice.Models.User;
import org.authenticationservice.authenticationservice.Models.UserSession;
import org.authenticationservice.authenticationservice.Repository.AuthRepository;
import org.authenticationservice.authenticationservice.Repository.UserSessionRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Primary
public class AuthService implements IAuthService {
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserSessionRepository userSessionRepository;
    private final SecretKey secretKey;
    public AuthService(AuthRepository authRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserSessionRepository userSessionRepository,
                       SecretKey secretKey) {
        this.authRepository = authRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userSessionRepository = userSessionRepository;
        this.secretKey = secretKey;
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
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", user.getEmail());
        payload.put("createdAt", System.currentTimeMillis());
        payload.put("expiresOn", System.currentTimeMillis() + (1000 * 20));
        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();
        // Persist the session data in the DB
        // create usesession and persist in DB
        UserSession userSession = new UserSession();
        userSession.setToken(token);
        userSession.setUser(user);
        userSession.setStatus(Status.ACTIVE);
        userSessionRepository.save(userSession);
        return Pair.of(userDTO, token);
    }

    // Validate Token as part of Resource server action
    public Boolean validateToken(long userId, String token) throws UserSessionNotFound {
        UserDTO userDTO = new UserDTO();
        Optional<UserSession> userSessionOptional = userSessionRepository.findUserSessionByUserIdAndToken(userId, token);
        if(userSessionOptional.isEmpty()) {
            throw new UserSessionNotFound("No sessions exists for the user");
        }
        UserSession userSession = userSessionOptional.get();
        return isValidToken(token, userSession);
//        return userDTO;
    }

    public UserDTO from(User user) {
        //HttpHeaders.
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    public boolean isValidToken(String token, UserSession userSession) {
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        Long expiresOn = (Long) claims.get("expiresOn");
        long now = System.currentTimeMillis();
        if(userSession.getStatus() == Status.INACTIVE) return false;
        if(now > expiresOn) {
            userSession.setStatus(Status.INACTIVE);
            userSessionRepository.save(userSession);
            return false;
        }
        return true;

    }
}
