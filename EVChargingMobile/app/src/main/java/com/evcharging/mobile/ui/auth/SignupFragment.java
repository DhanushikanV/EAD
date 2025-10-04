package com.evcharging.mobile.ui.auth;

import android.content.Context;
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
import com.evcharging.mobile.network.models.SignupRequest;
import com.evcharging.mobile.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Signup Fragment
 * 
 * Handles user registration functionality with comprehensive validation.
 */
public class SignupFragment extends Fragment {

    private TextInputEditText etNIC, etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private MaterialButton btnSignup, btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        initializeViews(view);
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        etNIC = view.findViewById(R.id.et_nic);
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        btnSignup = view.findViewById(R.id.btn_signup);
        btnLogin = view.findViewById(R.id.btn_login);
    }

    private void setupClickListeners() {
        btnSignup.setOnClickListener(v -> handleSignup());
        btnLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void handleSignup() {
        Log.d("SignupFragment", "handleSignup() called");
        String nic = etNIC.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        Log.d("SignupFragment", "Email: " + email + ", Password: " + password);
        
        if (validateInput(nic, name, email, phone, password, confirmPassword)) {
            Log.d("SignupFragment", "Validation passed, calling performSignup");
            performSignup(nic, name, email, phone, password);
        } else {
            Log.d("SignupFragment", "Validation failed");
        }
    }

    private boolean validateInput(String nic, String name, String email, String phone, String password, String confirmPassword) {
        if (!ValidationUtils.isValidNIC(nic)) {
            etNIC.setError("Please enter a valid NIC");
            return false;
        }

        if (name.length() < 2) {
            etName.setError("Name must be at least 2 characters");
            return false;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            etEmail.setError("Please enter a valid email");
            return false;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            etPhone.setError("Please enter a valid phone number");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private void performSignup(String nic, String name, String email, String phone, String password) {
        Log.d("SignupFragment", "performSignup() called with email: " + email);
        Context context = getContext();
        if (context == null) {
            Log.e("SignupFragment", "Context is null!");
            return;
        }

        Log.d("SignupFragment", "Creating AuthService...");
        AuthService authService = ApiClient.getRetrofitInstance(context).create(AuthService.class);
        
        // Create signup request matching backend EVOwner model
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setNic(nic);
        signupRequest.setName(name);
        signupRequest.setEmail(email);
        signupRequest.setPhone(phone);
        signupRequest.setPasswordHash(password); // Backend expects this field
        signupRequest.setStatus("Active");
        
        // Add default EV model
        java.util.List<String> evModels = new java.util.ArrayList<>();
        evModels.add("Tesla Model 3"); // Default EV model
        signupRequest.setEvModels(evModels);

        Log.d("SignupFragment", "Sending signup request: " + signupRequest.getEmail());
        
        authService.signup(signupRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("SignupFragment", "Response received: " + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    String errorMsg = "Registration failed: " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e("SignupFragment", "Error reading error body", e);
                        }
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("SignupFragment", "Signup failed: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("SignupFragment", "Network error", t);
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}