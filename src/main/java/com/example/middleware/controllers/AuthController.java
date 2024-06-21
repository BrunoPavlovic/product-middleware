package com.example.middleware.controllers;

import com.example.middleware.model.User;
import com.example.middleware.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController ( AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(){
        User user = authService.login();

        if(user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Authentication failed!", HttpStatus.UNAUTHORIZED);
        }
    }
}
