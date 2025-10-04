package com.evcharging.mobile.db.entities;

/**
 * UserLocal Entity
 * 
 * This entity represents the local user data stored in SQLite database.
 * It contains user authentication information and profile data for offline access.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class UserLocal {
    
    private String nic;
    private String name;
    private String email;
    private String phone;
    private String status;
    private String authToken;
    private long lastSyncAt;

    /**
     * Default constructor
     */
    public UserLocal() {
    }

    /**
     * Constructor with all parameters
     * 
     * @param nic User NIC (National ID)
     * @param name User full name
     * @param email User email address
     * @param phone User phone number
     * @param status User status (Active/Inactive)
     * @param authToken JWT authentication token
     * @param lastSyncAt Last synchronization timestamp
     */
    public UserLocal(String nic, String name, String email, String phone, 
                     String status, String authToken, long lastSyncAt) {
        this.nic = nic;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.authToken = authToken;
        this.lastSyncAt = lastSyncAt;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public long getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(long lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }
}
