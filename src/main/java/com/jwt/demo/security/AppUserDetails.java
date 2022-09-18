package com.jwt.demo.security;

import com.jwt.demo.model.User;
import com.jwt.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AppUserDetails implements UserDetailsService {

    public static final Logger logger = LoggerFactory.getLogger(AppUserDetails.class);

    private final UserRepository userRepository;

    public AppUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByUsername(username);

        optUser.ifPresentOrElse(
            (user1) -> {
                logger.info("User with Username: ${} Found", user1.getUsername());
            },
            () -> {
                throw new UsernameNotFoundException("User " + username + "Not Found");
            }
        );

        final User user = optUser.get();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getUserRoles())
                .build();
    }
}
