package com.evcharging.mobile.network.api;

import com.evcharging.mobile.network.models.AuthResponse;
import com.evcharging.mobile.network.models.LoginRequest;
import com.evcharging.mobile.network.models.SignupRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * AuthService Interface
 * 
 * This service interface defines authentication-related API endpoints.
 * It handles user login, signup, and other authentication operations.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public interface AuthService {

    /**
     * User login endpoint
     * 
     * @param request Login request containing email and password
     * @return Call<AuthResponse> containing JWT token and user info
     */
    @POST("user/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    /**
     * User signup endpoint
     * 
     * @param request Signup request containing user details
     * @return Call<AuthResponse> containing JWT token and user info
     */
    @POST("user")
    Call<AuthResponse> signup(@Body SignupRequest request);

    /**
     * Station operator login endpoint
     * 
     * @param request Login request containing email and password
     * @return Call<AuthResponse> containing JWT token and operator info
     */
    @POST("user/login")
    Call<AuthResponse> operatorLogin(@Body LoginRequest request);
}
