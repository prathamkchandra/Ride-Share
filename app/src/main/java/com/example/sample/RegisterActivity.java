package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText usernameEditText;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseAuth mAuth; // Firebase Auth instance
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        nameEditText = findViewById(R.id.name);
        usernameEditText = findViewById(R.id.username);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.login_link);

        // Set up click listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Set up click listener for the login link
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for valid email and password length
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Save additional user information to Firebase Realtime Database
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Create a new user profile object
                            UserProfile userProfile = new UserProfile(name, username, email, null);

                            // Save the user's profile information in the database
                            mDatabase.child("users").child(user.getUid()).setValue(userProfile)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                                            // Navigate to Role Selection Activity
                                            Intent intent = new Intent(RegisterActivity.this, RoleSelectionActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Error saving profile", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Handle error
                        Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
