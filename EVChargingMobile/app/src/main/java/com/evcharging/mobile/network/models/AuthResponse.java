package com.evcharging.mobile.network.models;

import com.squareup.moshi.Json;

/**
 * AuthResponse Model
 * 
 * This model represents the response from authentication API calls.
 * It contains the JWT token and user information after successful login.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class AuthResponse {

    @Json(name = "token")
    private String token;

    @Json(name = "user")
    private UserInfo user;

    /**
     * Default constructor
     */
    public AuthResponse() {
    }

    /**
     * Constructor with parameters
     * 
     * @param token JWT authentication token
     * @param user User information
     */
    public AuthResponse(String token, UserInfo user) {
        this.token = token;
        this.user = user;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    /**
     * UserInfo Inner Class
     * Represents user information in authentication response
     */
    public static class UserInfo {
        
        @Json(name = "id")
        private String id;
        
        @Json(name = "username")
        private String username;
        
        @Json(name = "email")
        private String email;
        
        @Json(name = "role")
        private String role;
        
        @Json(name = "status")
        private String status;

        /**
         * Default constructor
         */
        public UserInfo() {
        }

        /**
         * Constructor with parameters
         */
        public UserInfo(String id, String username, String email, String role, String status) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
            this.status = status;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

