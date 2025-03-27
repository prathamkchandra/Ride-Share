package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SourceDestinationActivity extends AppCompatActivity {

    private EditText sourceEditText, destinationEditText;
    private Button findMatchesButton;

    private DatabaseReference usersRef, destinationRef;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_destination);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        destinationRef = database.getReference("destinations");

        // Retrieve the role passed from RoleSelectionActivity
        role = getIntent().getStringExtra("ROLE");

        // Initialize views
        sourceEditText = findViewById(R.id.sourceEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        findMatchesButton = findViewById(R.id.findMatchesButton);

        // Set up button click listener
        findMatchesButton.setOnClickListener(view -> saveSourceAndDestination());
    }

    private void saveSourceAndDestination() {
        String source = sourceEditText.getText().toString().trim();
        String destination = destinationEditText.getText().toString().trim();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (!source.isEmpty() && !destination.isEmpty()) {
                String uid = user.getUid();

                // Save source and destination to the user's profile
                usersRef.child(uid).child("source").setValue(source);
                usersRef.child(uid).child("destination").setValue(destination);

                // Add user to the destination-specific node based on their role
                DatabaseReference roleRef = destinationRef.child(destination).child(role.toLowerCase());
                roleRef.child(uid).setValue(true)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Details saved!", Toast.LENGTH_SHORT).show();
                            navigateToMatchedUsersActivity(destination, role);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to save details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Navigate to MatchedUsersActivity and pass destination and role
     */
    private void navigateToMatchedUsersActivity(String destination, String role) {
        Intent intent = new Intent(SourceDestinationActivity.this, MatchedUsersActivity.class);
        intent.putExtra("DESTINATION", destination);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        finish();
    }
}
