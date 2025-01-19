package org.authenticationservice.authenticationservice.Models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "AccessRole")
public class Role extends BaseModel {
    private String value;
}
