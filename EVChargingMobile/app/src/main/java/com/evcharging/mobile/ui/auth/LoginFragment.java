package com.evcharging.mobile.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.evcharging.mobile.R;
import com.evcharging.mobile.network.ApiClient;
import com.evcharging.mobile.network.api.AuthService;
import com.evcharging.mobile.network.models.AuthResponse;
import com.evcharging.mobile.network.models.LoginRequest;
import com.evcharging.mobile.ui.DashboardActivity;
import com.evcharging.mobile.ui.OperatorActivity;
import com.evcharging.mobile.utils.SharedPreferencesManager;
import com.evcharging.mobile.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Login Fragment
 * 
 * Handles user login functionality with email and password validation.
 */
public class LoginFragment extends Fragment {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnSignup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initializeViews(view);
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnSignup = view.findViewById(R.id.btn_signup);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        btnSignup.setOnClickListener(v -> navigateToSignup());
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInput(email, password)) {
            performLogin(email, password);
        }
    }

    private void performLogin(String email, String password) {
        Context context = getContext();
        if (context == null) return;

        // Show loading state
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        Log.d("LoginFragment", "Attempting login for: " + email);
        
        // Create login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Create AuthService and make API call
        AuthService authService = ApiClient.getRetrofitInstance(context).create(AuthService.class);
        
        // Try EVOwner login first, then operator login
        Call<AuthResponse> call = authService.evOwnerLogin(loginRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");
                
                Log.d("LoginFragment", "Response received: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    handleLoginSuccess(authResponse);
                } else {
                    // If EVOwner login failed, try operator login
                    Log.d("LoginFragment", "EVOwner login failed, trying operator login");
                    tryOperatorLogin(authService, loginRequest);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");
                
                String errorMsg = "Network error: " + t.getMessage();
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                Log.e("LoginFragment", "Login network error", t);
            }
        });
    }

    private void tryOperatorLogin(AuthService authService, LoginRequest loginRequest) {
        Context context = getContext();
        if (context == null) return;

        Call<AuthResponse> operatorCall = authService.operatorLogin(loginRequest);
        operatorCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("LoginFragment", "Operator login response: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    handleLoginSuccess(authResponse);
                } else {
                    String errorMsg = "Login failed: " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = "Login failed: " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e("LoginFragment", "Error reading error body", e);
                        }
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("LoginFragment", "Both login attempts failed: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                Log.e("LoginFragment", "Operator login network error", t);
            }
        });
    }

    private void handleLoginSuccess(AuthResponse authResponse) {
        Context context = getContext();
        if (context == null) return;

        // Save user data to SharedPreferences
        SharedPreferencesManager prefsManager = new SharedPreferencesManager(context);
        
        if (authResponse.getUser() != null) {
            AuthResponse.UserInfo user = authResponse.getUser();
            prefsManager.saveUserData(
                user.getId() != null ? user.getId() : "", // Using ID instead of NIC
                user.getUsername() != null ? user.getUsername() : "", // Using username instead of name
                user.getEmail() != null ? user.getEmail() : "",
                "", // Phone not available in backend response
                authResponse.getToken() != null ? authResponse.getToken() : "",
                user.getRole() != null ? user.getRole() : ""
            );
            
            Log.d("LoginFragment", "User role: " + user.getRole());
            
            // Navigate based on user role
            navigateBasedOnRole(user.getRole());
        } else {
            Toast.makeText(context, "Login successful but no user data received", Toast.LENGTH_SHORT).show();
            navigateToDashboard();
        }
    }

    private void navigateBasedOnRole(String role) {
        Context context = getContext();
        if (context == null) return;

        Log.d("LoginFragment", "Navigating based on role: " + role);
        
        if ("Operator".equalsIgnoreCase(role)) {
            Toast.makeText(context, "Welcome Operator!", Toast.LENGTH_SHORT).show();
            navigateToOperatorDashboard();
        } else {
            Toast.makeText(context, "Welcome EV Owner!", Toast.LENGTH_SHORT).show();
            navigateToDashboard();
        }
    }

    private void navigateToOperatorDashboard() {
        Intent intent = new Intent(getActivity(), OperatorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private boolean validateInput(String email, String password) {
        if (!ValidationUtils.isValidEmail(email)) {
            etEmail.setError("Please enter a valid email");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void navigateToSignup() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SignupFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}