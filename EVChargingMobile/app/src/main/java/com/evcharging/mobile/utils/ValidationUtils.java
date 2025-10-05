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
     * Accepts both formats:
     * - Fully numerical: 12 digits (e.g., "199810400015")
     * - Numerical with one alphabet: 9 digits + V/v (e.g., "981040015v")
     */
    public static boolean isValidNIC(String nic) {
        if (nic == null) return false;
        // Remove any whitespace
        nic = nic.trim();
        
        // Format 1: Fully numerical (12 digits) - New NIC format
        if (nic.matches("\\d{12}")) {
            return true;
        }
        
        // Format 2: 9 digits with V or v at the end - Old NIC format
        if (nic.matches("\\d{9}[Vv]")) {
            return true;
        }
        
        return false;
    }

    /**
     * Validates password strength
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}