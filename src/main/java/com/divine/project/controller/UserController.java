package com.divine.project.controller;

import com.divine.project.model.user.User;
import com.divine.project.exception.ResourceNotFoundException;
import com.divine.project.payload.requests.UpdateUserRequest;
import com.divine.project.repository.UserRepository;
import com.divine.project.security.CurrentUser;
import com.divine.project.security.UserPrincipal;
import com.divine.project.service.serviceImpl.UserServiceImplementation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;
    private UserServiceImplementation userServiceImplementation;

    public UserController(UserRepository userRepository, UserServiceImplementation userServiceImplementation) {
        this.userRepository = userRepository;
        this.userServiceImplementation = userServiceImplementation;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> byId = userRepository.findById(userPrincipal.getId());

        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @PutMapping
    public User editUser(@Valid @RequestBody UpdateUserRequest updateUserRequest,
                         @CurrentUser UserPrincipal userPrincipal){
        User user = userRepository.findById(userPrincipal.getId()).get();
        User user1 = userServiceImplementation.editUser(user, updateUserRequest);
        return user1;
    }

}
