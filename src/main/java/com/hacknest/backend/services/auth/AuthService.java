package com.hacknest.backend.services.auth;

import com.hacknest.backend.dto.auth.AuthResponse;
import com.hacknest.backend.dto.auth.LoginRequest;
import com.hacknest.backend.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    
    String forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
