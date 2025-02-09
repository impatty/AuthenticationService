package org.authenticationservice.authenticationservice.Models;

import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserSession extends BaseModel{
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String token;
}
//1: 1
//M : 1
