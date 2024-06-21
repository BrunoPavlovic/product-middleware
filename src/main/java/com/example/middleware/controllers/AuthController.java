package com.example.middleware.controllers;

import com.example.middleware.model.User;
import com.example.middleware.model.UserDetails;
import com.example.middleware.services.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<?> login(@RequestBody JsonNode request){
        logger.info("User login");
        User user = authService.login(request);

        if(user != null) {
            logger.info("Successful login");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.error("Authentication failed");
            return new ResponseEntity<>("Authentication failed!", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(@RequestHeader("Authorization") String authorizationHeader){
        logger.info("Fetching information about current user");
        String token = authorizationHeader.substring("Bearer ".length());
        UserDetails user = authService.getCurrentAuthUser(token);
        if(user != null){
            logger.debug("Current user with username {} is logged in" , user.getUsername());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.warn("No current logged user");
            return new ResponseEntity<>("No current logged user!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody JsonNode request){
        logger.info("Sending request for extending a session with refresh token");
        Map<String,String> updatedTokens = authService.refreshToken(request);
        if(!updatedTokens.isEmpty()){
            logger.debug("Session is extending and new tokens are created");
            return new ResponseEntity<>(updatedTokens, HttpStatus.OK);
        } else {
            logger.error("Error while extending session");
            return new ResponseEntity<>("Error while extending session ", HttpStatus.BAD_REQUEST);
        }
    }
}
