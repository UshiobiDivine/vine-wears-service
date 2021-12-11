package com.divine.project.payload.requests;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserChangePasswordRequest {

    @NotBlank
    @Size(min = 6, max = 16, message = "password must be between 6 - 16 characters")
    @NotNull(message = "please enter your old password")
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 16, message = "password must be between 6 - 16 characters")
    @NotNull(message = "please enter your new password")
    private String newPassword;

    @NotBlank
    @Size(min = 6, max = 16, message = "password must be between 6 - 16 characters")
    @NotNull(message = "please confirm your new password")
    private String confirmNewPassword;
}
