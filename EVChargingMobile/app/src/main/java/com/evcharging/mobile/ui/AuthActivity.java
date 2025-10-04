package com.evcharging.mobile.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.auth.LoginFragment;

/**
 * Authentication Activity
 * 
 * Hosts the login and signup fragments for user authentication.
 */
public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Load login fragment by default
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new LoginFragment());
            transaction.commit();
        }
    }
}
