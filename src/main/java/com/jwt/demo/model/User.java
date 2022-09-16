package com.jwt.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;
import java.util.List;

@Document
@Data
public class User {

    @Id
    private Integer id;

    @Size(min = 4, max = 16, message = "Username length must between 4 and 16 char")
    private String username;

    private String email;

    @Size(min = 4, message = "Password length must at least 4 char")
    private String password;

    private List<Role> userRoles;

}
