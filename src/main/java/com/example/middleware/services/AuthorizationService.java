package com.example.middleware.services;

import com.example.middleware.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Autowired
    private AuthService authService;

    public boolean isLoggedIn(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            UserDetails user = authService.getCurrentAuthUser(token);
            return user != null;
        }
        return false;
    }
}
