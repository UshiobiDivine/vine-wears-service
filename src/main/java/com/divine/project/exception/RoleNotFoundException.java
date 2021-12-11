package com.divine.project.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends RuntimeException {



    private String message;

    public RoleNotFoundException(String message) {
        super(message);

    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
