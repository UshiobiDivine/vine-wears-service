package com.divine.project.service;

import com.divine.project.payload.requests.LoginRequest;

public interface AuthService {
   boolean authenticateUser(LoginRequest loginRequest);
}
