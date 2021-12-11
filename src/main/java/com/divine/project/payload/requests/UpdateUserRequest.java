package com.divine.project.payload.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UpdateUserRequest {

    @NotNull(message = "name cannot be empty")
    private String name;

    @NotNull(message = "email cannot be empty")
    @Email(message = "must be an email")
    private String email;

}
