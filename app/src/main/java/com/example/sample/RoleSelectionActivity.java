package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoleSelectionActivity extends AppCompatActivity {

    private Button riderButton, passengerButton;
    private DatabaseReference usersRef, destinationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        destinationsRef = database.getReference("destinations");

        // Initialize buttons
        riderButton = findViewById(R.id.riderButton);
        passengerButton = findViewById(R.id.passengerButton);

        // Set button click listeners
        riderButton.setOnClickListener(v -> saveUserRole("Rider"));
        passengerButton.setOnClickListener(v -> saveUserRole("Passenger"));
    }

    /**
     * Save the user's selected role to Firebase Realtime Database
     *
     * @param role - "Rider" or "Passenger"
     */
    private void saveUserRole(String role) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
            String name = user.getDisplayName() != null ? user.getDisplayName() : "Unknown";
            String email = user.getEmail();

            // Create a user object to store the details
            ; // Destination is null initially
            UserProfile userProfile = new UserProfile(uid, name, email, role);
// Then set destination separately if needed
            userProfile.setDestination(null);
            // Save the user profile in the "users" node
            usersRef.child(uid).setValue(userProfile)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, role + " selected successfully!", Toast.LENGTH_SHORT).show();
                        navigateToSourceDestination(role);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to save role: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not authenticated. Please log in again.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigate to SourceDestinationActivity with the role passed as extra
     *
     * @param role - "Rider" or "Passenger"
     */
    private void navigateToSourceDestination(String role) {
        Intent intent = new Intent(RoleSelectionActivity.this, SourceDestinationActivity.class);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        finish();
    }
}
