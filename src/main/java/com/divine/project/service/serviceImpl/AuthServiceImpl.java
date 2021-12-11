package com.divine.project.service.serviceImpl;

import com.divine.project.model.user.User;
import com.divine.project.payload.requests.LoginRequest;
import com.divine.project.repository.UserRepository;
import com.divine.project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean authenticateUser(LoginRequest loginRequest) {

        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword());

        if (user.isPresent() &&
                user.get().getEmail().equals(loginRequest.getEmail()) && passwordMatches){
            return true;
        }
        return false;
    }
}
