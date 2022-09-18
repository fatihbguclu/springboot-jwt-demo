package com.jwt.demo.dto;

import com.jwt.demo.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {

    private Integer id;
    private String username;
    private String email;
    List<Role> userRoles;

}
