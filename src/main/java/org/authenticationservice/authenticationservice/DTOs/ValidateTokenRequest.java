package org.authenticationservice.authenticationservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequest{
    private long userId;
    private String token;
}
