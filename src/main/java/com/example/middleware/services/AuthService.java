package com.example.middleware.services;

import com.example.middleware.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    private static final String LOGIN_URL = "https://dummyjson.com/auth/login";
    private static final String CURRENT_USER_URL = "https://dummyjson.com/auth/me";
    private static final String REFRESH_TOKEN_URL = "https://dummyjson.com/auth/refresh";

    private static final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger logger = LogManager.getLogger(AuthService.class);
    private User currentUser;

    public User login(){
        logger.info("Sending login request");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JsonNode request = mapper.createObjectNode()
                .put("username", "emilys")
                .put("password", "emilyspass")
                .put("expiresInMins", 15);

        HttpEntity<JsonNode> entity = new HttpEntity<>(request, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, entity, JsonNode.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            currentUser = mapper.convertValue(response.getBody(), new TypeReference<>() {});
            return currentUser;
        }

        logger.error("Authentication failed: {}", response.getStatusCode());
        throw new RuntimeException("Authentication failed with status: " + response.getStatusCode());
    }
}
