package com.evcharging.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences Manager
 * 
 * This utility class manages all SharedPreferences operations for the application.
 * It provides methods to store and retrieve user authentication data, settings,
 * and other persistent data.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class SharedPreferencesManager {

    private static final String PREF_NAME = "EVChargingPrefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_NIC = "user_nic";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_LAST_SYNC = "last_sync";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    /**
     * Constructor
     * 
     * @param context Application context
     */
    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Check if user is logged in
     * 
     * @return true if user is logged in, false otherwise
     */
    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Set user login status
     * 
     * @param isLoggedIn Login status
     */
    public void setUserLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    /**
     * Get user NIC
     * 
     * @return User NIC
     */
    public String getUserNIC() {
        return sharedPreferences.getString(KEY_USER_NIC, null);
    }

    /**
     * Set user NIC
     * 
     * @param nic User NIC
     */
    public void setUserNIC(String nic) {
        editor.putString(KEY_USER_NIC, nic);
        editor.apply();
    }

    /**
     * Get user name
     * 
     * @return User name
     */
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    /**
     * Set user name
     * 
     * @param name User name
     */
    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    /**
     * Get user email
     * 
     * @return User email
     */
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    /**
     * Set user email
     * 
     * @param email User email
     */
    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    /**
     * Get user phone
     * 
     * @return User phone
     */
    public String getUserPhone() {
        return sharedPreferences.getString(KEY_USER_PHONE, null);
    }

    /**
     * Set user phone
     * 
     * @param phone User phone
     */
    public void setUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    /**
     * Get authentication token
     * 
     * @return Auth token
     */
    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    /**
     * Set authentication token
     * 
     * @param token Auth token
     */
    public void setAuthToken(String token) {
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    /**
     * Get user role
     * 
     * @return User role
     */
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, null);
    }

    /**
     * Set user role
     * 
     * @param role User role
     */
    public void setUserRole(String role) {
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    /**
     * Get last sync timestamp
     * 
     * @return Last sync timestamp
     */
    public long getLastSync() {
        return sharedPreferences.getLong(KEY_LAST_SYNC, 0);
    }

    /**
     * Set last sync timestamp
     * 
     * @param timestamp Sync timestamp
     */
    public void setLastSync(long timestamp) {
        editor.putLong(KEY_LAST_SYNC, timestamp);
        editor.apply();
    }

    /**
     * Clear all user data (logout)
     */
    public void clearUserData() {
        editor.clear();
        editor.apply();
    }

    /**
     * Save complete user data
     * 
     * @param nic User NIC
     * @param name User name
     * @param email User email
     * @param phone User phone
     * @param token Auth token
     * @param role User role
     */
    public void saveUserData(String nic, String name, String email, String phone, String token, String role) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_NIC, nic);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.putString(KEY_USER_ROLE, role);
        editor.putLong(KEY_LAST_SYNC, System.currentTimeMillis());
        editor.apply();
    }
}

