package com.divine.project.controller;

import com.divine.project.model.user.AuthProvider;
import com.divine.project.model.user.User;
import com.divine.project.payload.responses.ApiResponse;
import com.divine.project.payload.requests.UserChangePasswordRequest;
import com.divine.project.payload.requests.UserForgotPasswordRequest;
import com.divine.project.repository.UserRepository;
import com.divine.project.security.CurrentUser;
import com.divine.project.security.UserPrincipal;
import com.divine.project.service.serviceImpl.UserServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class PasswordResetController {

    private UserRepository userRepository;
    private UserServiceImplementation userServiceImplementation;

    public PasswordResetController(UserRepository userRepository, UserServiceImplementation userServiceImplementation) {
        this.userRepository = userRepository;
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/sendCode/{userEmail}")
    public ResponseEntity<ApiResponse> sendPasswordResetCode(@PathVariable(name = "userEmail") String email ){

        boolean sent = userServiceImplementation.sendAndSaveVerificationCode(email);
        if (sent){
            ApiResponse apiResponse = new ApiResponse(sent, "Password reset code has been sent, please check your email");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        ApiResponse apiResponse = new ApiResponse(sent, "Sorry, we could not find any account with that email address");
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/reset_code/{code}/{email}")
    public ResponseEntity<ApiResponse> checkVerificationCode(@PathVariable(name = "code") String code,
                                                             @PathVariable(name = "email") String email) throws ParseException {
        Optional<User> user = userRepository.findByEmail(email.toLowerCase());
        boolean verified = userServiceImplementation.checkVerificationCode(user.get(), code);
        if (verified){
            ApiResponse apiResponse = new ApiResponse(verified, "Valid code");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        user.get().setVerificationCode(null);
        userRepository.save(user.get());
        ApiResponse apiResponse = new ApiResponse(verified, "Code is NOT valid");
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/password_reset/{email}")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody UserForgotPasswordRequest passwordRequest,
                                                     @PathVariable(name = "email") String email){
        User user = userRepository.findByEmail(email.toLowerCase()).get();
        boolean successful = userServiceImplementation.userForgotPassword(user, passwordRequest);
        if (!successful){
            return new ResponseEntity<>(new ApiResponse(!successful, "Sorry could not reset password"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(successful, "Password reset successful"), HttpStatus.OK);
    }

      @PostMapping("/password_change")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody UserChangePasswordRequest passwordRequest,
                                                      @CurrentUser UserPrincipal userPrincipal){

          User user = userRepository.findById(userPrincipal.getId()).get();
          if (user.getProvider().equals(AuthProvider.google)){
              return new ResponseEntity<>(new ApiResponse(false, "You are logged in with google, this account is not tied to a password"), HttpStatus.BAD_REQUEST);
          }
        boolean successful = userServiceImplementation.userChangePassword(user, passwordRequest);
        if (!successful){
            return new ResponseEntity<>(new ApiResponse(!successful, "Sorry could not reset password"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(successful, "Password reset successful"), HttpStatus.OK);
    }


}
