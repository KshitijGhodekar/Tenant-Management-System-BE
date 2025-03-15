package com.tenant.management.userLogin.services;

import com.tenant.management.userLogin.entities.User;
import com.tenant.management.userLogin.repositories.UserRepository;
import com.tenant.management.userLogin.requestdtos.LoginRequest;
import com.tenant.management.userLogin.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginRequest loginRequest) {
        logger.info("Searching for user with username: {}", loginRequest.getUsername());
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("Database Password: {}", user.getPassword());
            logger.info("Provided Password: {}", loginRequest.getPassword());
            if (loginRequest.getPassword().equals(user.getPassword())) { // Plain text comparison (not recommended for production)
                return jwtUtil.generateToken(user.getUsername()); // Return JWT token
            }
        }

        return null;
    }
}
