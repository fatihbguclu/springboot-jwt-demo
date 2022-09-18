package com.jwt.demo.controller;

import com.jwt.demo.dto.UserDto;
import com.jwt.demo.dto.UserResponseDto;
import com.jwt.demo.mapper.UserMapper;
import com.jwt.demo.mapper.UserResponseMapper;
import com.jwt.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserResponseMapper userResponseMapper;

    public UserController(UserService userService, UserMapper userMapper, UserResponseMapper userResponseMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping("/signin")
    public String signIn(@RequestParam String username, @RequestParam String password){
        return userService.signIn(username,password);
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody UserDto userDto){
        return userService.signUp(userMapper.toUser(userDto));
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable String username){
        userService.deleteUser(username);
        return username;
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDto getUser(@PathVariable String username){
        return userResponseMapper.toUserResponseDto(userService.getUser(username));
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public String refresh(HttpServletRequest request){
        return userService.refreshToken(request.getRemoteUser());
    }

}
