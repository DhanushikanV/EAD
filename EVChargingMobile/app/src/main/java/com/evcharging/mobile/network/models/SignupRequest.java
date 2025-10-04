package com.evcharging.mobile.network.models;

import com.squareup.moshi.Json;

/**
 * SignupRequest Model
 * 
 * This model represents the signup request payload sent to the registration API.
 * It contains user information for account creation.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class SignupRequest {

    @Json(name = "username")
    private String username;

    @Json(name = "email")
    private String email;

    @Json(name = "password")
    private String password;

    @Json(name = "passwordHash")
    private String passwordHash;

    @Json(name = "role")
    private String role;

    @Json(name = "status")
    private String status;

    /**
     * Default constructor
     */
    public SignupRequest() {
    }

    /**
     * Constructor with parameters
     * 
     * @param username User username
     * @param email User email address
     * @param password User password
     * @param role User role
     * @param status User status
     */
    public SignupRequest(String username, String email, String password, String role, String status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
