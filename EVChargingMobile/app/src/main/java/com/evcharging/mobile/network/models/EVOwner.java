package com.evcharging.mobile.network.models;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * EVOwner Model
 * 
 * This model represents the EV Owner entity from the backend API.
 * It contains all EV owner information including profile and vehicle data.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class EVOwner {

    @Json(name = "NIC")
    private String nic;

    @Json(name = "Name")
    private String name;

    @Json(name = "Email")
    private String email;

    @Json(name = "Phone")
    private String phone;

    @Json(name = "PasswordHash")
    private String passwordHash;

    @Json(name = "Status")
    private String status;

    @Json(name = "CreatedAt")
    private String createdAt;

    @Json(name = "EVModels")
    private List<String> evModels;

    /**
     * Default constructor
     */
    public EVOwner() {
    }

    /**
     * Constructor with parameters
     * 
     * @param nic User NIC (National ID)
     * @param name User full name
     * @param email User email address
     * @param phone User phone number
     * @param passwordHash Hashed password
     * @param status User status (Active/Inactive)
     * @param createdAt Creation timestamp
     * @param evModels List of EV models
     */
    public EVOwner(String nic, String name, String email, String phone, 
                   String passwordHash, String status, String createdAt, 
                   List<String> evModels) {
        this.nic = nic;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.status = status;
        this.createdAt = createdAt;
        this.evModels = evModels;
    }

    // Getters and Setters
    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getEvModels() {
        return evModels;
    }

    public void setEvModels(List<String> evModels) {
        this.evModels = evModels;
    }
}

