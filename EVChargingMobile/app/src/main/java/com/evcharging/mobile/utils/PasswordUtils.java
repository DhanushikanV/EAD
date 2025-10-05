package com.evcharging.mobile.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password Utilities
 * 
 * This utility class provides password hashing functionality for the mobile app.
 * It uses SHA-256 with salt for password hashing.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class PasswordUtils {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    /**
     * Hash a password with a random salt
     * 
     * @param password Plain text password
     * @return Hashed password with salt
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash password with salt
            String hashedPassword = hashPasswordWithSalt(password, salt);

            // Return salt + hash (salt is prepended for storage)
            return Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    /**
     * Hash a password with a specific salt
     * 
     * @param password Plain text password
     * @param salt Salt bytes
     * @return Hashed password
     * @throws NoSuchAlgorithmException If SHA-256 is not available
     */
    private static String hashPasswordWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        md.update(salt);
        byte[] hashedBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    /**
     * Verify a password against a hash
     * 
     * @param password Plain text password
     * @param storedHash Stored hash (salt:hash format)
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Split salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String hash = hashPasswordWithSalt(password, salt);

            return hash.equals(parts[1]);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Simple hash for development/testing (not secure for production)
     * This creates a simple hash that matches the backend's expectation
     * 
     * @param password Plain text password
     * @return Simple hashed password
     */
    public static String simpleHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple encoding
            return password + "_hashed";
        }
    }
}
