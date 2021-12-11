package com.divine.project.controller;

import com.divine.project.payload.responses.ApiResponse;
import com.divine.project.repository.UserRepository;
import com.divine.project.service.serviceImpl.UserServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private UserRepository userRepository;
    private UserServiceImplementation userServiceImplementation;

    public AdminController(UserRepository userRepository, UserServiceImplementation userServiceImplementation) {
        this.userRepository = userRepository;
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/allusers")
    public ResponseEntity<List> getAllUsers(){
        return null;
    }

    @PutMapping("/admin_role/{userId}")
    public ResponseEntity<ApiResponse> giveAdminRole(@PathVariable(name = "userId") Long userId){
        return new ResponseEntity<>(new ApiResponse(userServiceImplementation.giveAdminRole(userId), "Admin role given"),
                HttpStatus.OK);
    }

    @PutMapping("/remove_admin_role/{userId}")
    public ResponseEntity<ApiResponse> removeAdminRole(@PathVariable(name = "userId") Long userId){
        return new ResponseEntity<>(new ApiResponse(userServiceImplementation.removeAdminRole(userId), "Admin role removed"),
                HttpStatus.OK);
    }
}
