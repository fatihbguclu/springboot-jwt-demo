package com.jwt.demo.service;

import com.jwt.demo.exception.CustomException;
import com.jwt.demo.model.User;
import com.jwt.demo.repository.UserRepository;
import com.jwt.demo.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    public String signIn(String username, String password){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

            Optional<User> optUser = userRepository.findByUsername(username);
            User user = optUser.orElseThrow(() -> new CustomException("ss", HttpStatus.BAD_REQUEST));

            return jwtProvider.getToken(username, user.getUserRoles());

        }catch (AuthenticationException ex){
            throw new CustomException("Used Username", HttpStatus.BAD_REQUEST);
        }
    }

    public String signUp(User user){
        boolean isExisted = userRepository.existsByUsername(user.getUsername());

        if(!isExisted){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtProvider.getToken(user.getUsername(),user.getUserRoles());
        }else {
            throw new CustomException("Used Username",HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteUser(String username){
        userRepository.deleteByUsername(username);
    }

    public User getUser(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);

        return optionalUser.orElseThrow(() -> {
            throw new CustomException("Used Username",HttpStatus.NOT_FOUND);
        });
    }

    public String refreshToken(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);

        User user = optionalUser.orElseThrow(() -> {
            throw new CustomException("Username Doesn't Exist",HttpStatus.BAD_REQUEST);
        });

        return jwtProvider.getToken(user.getUsername(),user.getUserRoles());
    }

}
