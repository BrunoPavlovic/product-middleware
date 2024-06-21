package com.example.middleware.services;

import com.example.middleware.model.User;
import com.example.middleware.model.UserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private static final String LOGIN_URL = "https://dummyjson.com/auth/login";
    private static final String CURRENT_USER_URL = "https://dummyjson.com/auth/me";
    private static final String REFRESH_TOKEN_URL = "https://dummyjson.com/auth/refresh";

    private static final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger logger = LogManager.getLogger(AuthService.class);

    public User login(JsonNode request){
        try {
            logger.info("Sending login request");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<JsonNode> entity = new HttpEntity<>(request, headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, entity, JsonNode.class);

            if(response.getStatusCode() == HttpStatus.OK) {
                return mapper.convertValue(response.getBody(), new TypeReference<>() {});
            }

            logger.error("Authentication failed: {}", response.getStatusCode());
            throw new RuntimeException("Authentication failed with status: " + response.getStatusCode());
        } catch (RuntimeException e) {
            logger.error("Runtime exception while user login: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserDetails getCurrentAuthUser(String token){
        try {
            logger.info("Fetching information about current user");
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<JsonNode> entity = new HttpEntity<>(headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(CURRENT_USER_URL, HttpMethod.GET, entity, JsonNode.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return mapper.convertValue(response.getBody(), new TypeReference<>() {});
            }

            logger.error("No current user : {}", response.getStatusCode());
            throw new RuntimeException("No current user - status code: " + response.getStatusCode());
        } catch (RuntimeException e) {
            logger.error("Runtime exception : {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<String, String> refreshToken(JsonNode request){
        try {
            logger.info("Extending session without username and password - refreshToken");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<JsonNode> entity = new HttpEntity<>(request, headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    REFRESH_TOKEN_URL, HttpMethod.POST, entity, JsonNode.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = response.getBody();
                Map<String, String> updatedTokens = new HashMap<>();
                updatedTokens.put("token", responseBody.get("token").asText());
                updatedTokens.put("refreshToken", responseBody.get("refreshToken").asText());
                return updatedTokens;
            } else {
                logger.error("Failed to refresh token - status: {}" , response.getStatusCode());
                throw new RuntimeException("Failed to refresh token: " + response.getStatusCode());
            }
        } catch (RuntimeException e) {
            logger.error("Runtime exception while refreshing token: {} ",  e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
