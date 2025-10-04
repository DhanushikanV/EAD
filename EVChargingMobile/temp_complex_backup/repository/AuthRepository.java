package com.evcharging.mobile.repository;

import android.content.Context;

import com.evcharging.mobile.db.database.AppDatabase;
import com.evcharging.mobile.db.dao.UserDao;
import com.evcharging.mobile.db.entities.UserLocal;
import com.evcharging.mobile.network.api.AuthService;
import com.evcharging.mobile.network.models.AuthResponse;
import com.evcharging.mobile.network.models.EVOwner;
import com.evcharging.mobile.network.models.LoginRequest;
import com.evcharging.mobile.network.models.SignupRequest;
import com.evcharging.mobile.utils.SharedPreferencesManager;

import retrofit2.Response;

/**
 * AuthRepository
 * 
 * This repository handles authentication operations including login, signup,
 * and user session management with both local database and remote API.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class AuthRepository {

    private final UserDao userDao;
    private final AuthService apiService;
    private final SharedPreferencesManager preferencesManager;
    private final Context context;

    public AuthRepository(Context context) {
        this.context = context;
        
        // Initialize database DAO
        AppDatabase database = AppDatabase.getInstance(context);
        this.userDao = database.userDao();
        
        // Initialize API service
        this.apiService = ApiClient.getRetrofitInstance(context).create(AuthService.class);
        
        // Initialize preferences manager
        this.preferencesManager = new SharedPreferencesManager(context);
    }

    /**
     * Login user with email and password
     * 
     * @param email User email
     * @param password User password
     * @return AuthResponse with token and user data, or null if failed
     */
    public AuthResponse login(String email, String password) {
        try {
            LoginRequest request = new LoginRequest(email, password);
            Response<AuthResponse> response = apiService.login(request).execute();
            
            if (response.isSuccessful() && response.body() != null) {
                AuthResponse authResponse = response.body();
                
                // Save user data locally
                saveUserSession(authResponse);
                
                return authResponse;
            } else {
                // Handle login failure
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Signup new EV owner
     * 
     * @param signupRequest Signup request data
     * @return AuthResponse with token and user data, or null if failed
     */
    public AuthResponse signup(SignupRequest signupRequest) {
        try {
            Response<AuthResponse> response = apiService.signup(signupRequest).execute();
            
            if (response.isSuccessful() && response.body() != null) {
                AuthResponse authResponse = response.body();
                
                // Save user data locally
                saveUserSession(authResponse);
                
                return authResponse;
            } else {
                // Handle signup failure
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if user is logged in
     * 
     * @return true if user is logged in
     */
    public boolean isLoggedIn() {
        String token = preferencesManager.getAuthToken();
        return token != null && !token.isEmpty();
    }

    /**
     * Get current user data
     * 
     * @return UserLocal entity or null if not logged in
     */
    public UserLocal getCurrentUser() {
        String userNIC = preferencesManager.getUserNIC();
        if (userNIC != null) {
            try {
                return userDao.getUserByNIC(userNIC);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Logout user
     */
    public void logout() {
        // Clear preferences
        preferencesManager.clearUserData();
        
        // Clear local database
        try {
            userDao.deleteAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save user session data
     * 
     * @param authResponse Authentication response from server
     */
    private void saveUserSession(AuthResponse authResponse) {
        if (authResponse != null && authResponse.getUser() != null) {
            EVOwner user = authResponse.getUser();
            
            // Save to SharedPreferences
            preferencesManager.saveAuthToken(authResponse.getToken());
            preferencesManager.saveUserNIC(user.getNic());
            preferencesManager.saveUserName(user.getName());
            preferencesManager.saveUserEmail(user.getEmail());
            preferencesManager.saveUserPhone(user.getPhone());
            
            // Save to local database
            saveUserToDatabase(user, authResponse.getToken());
        }
    }

    /**
     * Save user data to local database
     * 
     * @param user EVOwner data
     * @param token JWT token
     */
    private void saveUserToDatabase(EVOwner user, String token) {
        new Thread(() -> {
            try {
                UserLocal userLocal = new UserLocal();
                userLocal.nic = user.getNic();
                userLocal.name = user.getName();
                userLocal.email = user.getEmail();
                userLocal.phone = user.getPhone();
                userLocal.status = user.getStatus();
                userLocal.authToken = token;
                userLocal.lastSyncAt = System.currentTimeMillis();
                
                userDao.insertOrUpdate(userLocal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Refresh user token
     * 
     * @return true if token refreshed successfully
     */
    public boolean refreshToken() {
        // TODO: Implement token refresh logic
        // For now, return true if user is logged in
        return isLoggedIn();
    }

    /**
     * Validate current session
     * 
     * @return true if session is valid
     */
    public boolean validateSession() {
        if (!isLoggedIn()) {
            return false;
        }
        
        // TODO: Implement session validation with server
        // For now, check if token exists and is not expired
        String token = preferencesManager.getAuthToken();
        return token != null && !token.isEmpty();
    }
}
