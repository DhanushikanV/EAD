package com.evcharging.mobile.utils;

import android.util.Patterns;

/**
 * Validation Utilities
 * 
 * Provides validation methods for user input fields.
 */
public class ValidationUtils {

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validates phone number format (basic validation)
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.length() >= 10 && phone.matches("\\d+");
    }

    /**
     * Validates NIC format (Sri Lankan NIC)
     */
    public static boolean isValidNIC(String nic) {
        if (nic == null) return false;
        // Basic NIC validation - 9 digits with optional V at end
        return nic.matches("\\d{9}[Vv]?");
    }

    /**
     * Validates password strength
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}