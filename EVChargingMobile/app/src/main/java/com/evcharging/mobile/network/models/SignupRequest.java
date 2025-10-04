package com.evcharging.mobile.network.models;

import com.squareup.moshi.Json;
import java.util.List;
import java.util.ArrayList;

/**
 * SignupRequest Model
 * 
 * This model represents the EV Owner signup request payload sent to the registration API.
 * It contains EV owner information for account creation.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class SignupRequest {

    @Json(name = "nic")
    private String nic;

    @Json(name = "name")
    private String name;

    @Json(name = "email")
    private String email;

    @Json(name = "phone")
    private String phone;

    @Json(name = "passwordHash")
    private String passwordHash;

    @Json(name = "status")
    private String status;

    @Json(name = "evModels")
    private List<String> evModels;

    /**
     * Default constructor
     */
    public SignupRequest() {
        this.evModels = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     * 
     * @param nic EV Owner NIC
     * @param name EV Owner name
     * @param email EV Owner email
     * @param phone EV Owner phone
     * @param passwordHash Hashed password
     * @param status Account status
     * @param evModels List of EV models
     */
    public SignupRequest(String nic, String name, String email, String phone, 
                        String passwordHash, String status, List<String> evModels) {
        this.nic = nic;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.status = status;
        this.evModels = evModels != null ? evModels : new ArrayList<>();
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

    public List<String> getEvModels() {
        return evModels;
    }

    public void setEvModels(List<String> evModels) {
        this.evModels = evModels;
    }
}
