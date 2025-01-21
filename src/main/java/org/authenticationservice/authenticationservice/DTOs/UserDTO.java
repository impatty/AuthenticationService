package org.authenticationservice.authenticationservice.DTOs;

import lombok.Getter;
import lombok.Setter;
import org.authenticationservice.authenticationservice.Models.Role;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String email;
    private List<Role> roles;

}
