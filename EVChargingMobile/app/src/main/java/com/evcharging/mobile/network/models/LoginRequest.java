package com.evcharging.mobile.network.models;

import com.squareup.moshi.Json;

/**
 * LoginRequest Model
 * 
 * This model represents the login request payload sent to the authentication API.
 * It contains user credentials for authentication.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class LoginRequest {

    @Json(name = "email")
    private String email;

    @Json(name = "password")
    private String password;

    /**
     * Default constructor
     */
    public LoginRequest() {
    }

    /**
     * Constructor with parameters
     * 
     * @param email User email address
     * @param password User password
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

