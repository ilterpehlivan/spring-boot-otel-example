package org.ilt.userservice.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserDTO {
    private String name;
    private String email;
}
