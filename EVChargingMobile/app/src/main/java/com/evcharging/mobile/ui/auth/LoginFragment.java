package com.evcharging.mobile.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.DashboardActivity;
import com.evcharging.mobile.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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
            // TODO: Implement actual login logic with API
            // For now, just show success and navigate to dashboard
            Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
            navigateToDashboard();
        }
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