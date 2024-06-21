package com.example.middleware.controllers;

import com.example.middleware.model.User;
import com.example.middleware.services.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    public AuthController ( AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(){
        logger.info("User login");
        User user = authService.login();

        if(user != null) {
            logger.info("Successful login");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.error("Authentication failed");
            return new ResponseEntity<>("Authentication failed!", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<String> currentUser(){
        logger.info("Fetching information about current user");
        String username = authService.getCurrentAuthUser();
        if(!username.isEmpty()){
            logger.debug("Current user with username {} is logged in" , username);
            return new ResponseEntity<>("Current user is: " + username, HttpStatus.OK);
        } else {
            logger.warn("No current logged user");
            return new ResponseEntity<>("No current logged user!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(){
        logger.info("Sending request for extending a session with refresh token");
        User user = authService.refreshToken();
        if(user != null){
            logger.debug("Session is extending and new tokens are created");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.error("Error while extending session");
            return new ResponseEntity<>("Error while extending session ", HttpStatus.BAD_REQUEST);
        }
    }
}
