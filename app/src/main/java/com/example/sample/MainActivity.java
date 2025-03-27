package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerLink;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);
        progressBar = findViewById(R.id.progress_bar);

        // Check if the user is already logged in, if yes, redirect to UserProfileActivity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, redirect to the user profile page
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
            finish(); // Close the login activity
        }

        // Set up click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // Set up click listener for the register link
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate user input
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        // Show progress bar while logging in
        progressBar.setVisibility(View.VISIBLE);

        // Sign in with Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Hide progress bar after login attempt
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(MainActivity.this, "Login Successful! Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

                            // Redirect to UserProfileActivity after successful login
                            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                            finish(); // Close the current activity (login activity)
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to retrieve user information.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle login failure
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Login failed.";
                        Toast.makeText(MainActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
