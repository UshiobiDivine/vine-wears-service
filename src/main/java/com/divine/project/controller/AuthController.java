package com.divine.project.controller;

import com.divine.project.exception.BadRequestException;
import com.divine.project.exception.RoleNotFoundException;
import com.divine.project.model.user.Role;
import com.divine.project.model.user.RoleEnum;
import com.divine.project.model.user.User;
import com.divine.project.model.user.AuthProvider;
import com.divine.project.payload.responses.ApiResponse;
import com.divine.project.payload.responses.AuthResponse;
import com.divine.project.payload.requests.LoginRequest;
import com.divine.project.payload.requests.SignUpRequest;
import com.divine.project.repository.RoleRepository;
import com.divine.project.repository.UserRepository;
import com.divine.project.security.TokenProvider;
import com.divine.project.service.AuthService;
import com.divine.project.util.CapcaseString;
import com.divine.project.util.mail.Mailjet;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthService authService;

    @Autowired
    private Mailjet mailjet;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        boolean authenticateUser = authService.authenticateUser(loginRequest);
        if (!authenticateUser){
            throw new BadCredentialsException("Invalid Login Details");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail().toLowerCase(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if(userRepository.existsByEmail(signUpRequest.getEmail().toLowerCase())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User user = new User();
        user.setName(CapcaseString.capitalizeString(signUpRequest.getName()));
        user.setEmail(signUpRequest.getEmail().toLowerCase());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        if(userRepository.count()==0) {
            List<Role> rolesList = new ArrayList<>();
            Optional<Role> adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
            Optional<Role> userRole = roleRepository.findByName(RoleEnum.ROLE_USER);
            if (adminRole.isPresent()&&userRole.isPresent()) {
                rolesList.add(adminRole.get());
                rolesList.add(userRole.get());
                user.setRoles(rolesList);
            }else {
                throw new RoleNotFoundException("Role could not be fetched from the database");
            }

        }else {
            List<Role> rolesList = new ArrayList<>();
            Optional<Role> userRole = roleRepository.findByName(RoleEnum.ROLE_USER);
            if (userRole.isPresent()) {
                rolesList.add(userRole.get());
                user.setRoles(rolesList);
            }else {
                throw new RoleNotFoundException("Role could not be fetched from the database");
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        String email = result.getEmail();
        String name = result.getName();
        String subject = "Registration Successful";
        String body = String.format("Hi %s \n\n Welcome to Vine wears!!! \n Your Registration was " +
                "successful, thank you for choosing us, vine wears has got you COVERED, Cheers!!!", name);
//        try {
//            mailjet.sendMail(email, name, subject, body);
//        } catch (MailjetSocketTimeoutException e) {
//            e.printStackTrace();
//        } catch (MailjetException e) {
//            e.printStackTrace();
//        }

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

}
