package com.jwt.demo.dto;

import com.jwt.demo.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String username;
    private String email;
    private String password;
    private List<Role> userRoles;

}
