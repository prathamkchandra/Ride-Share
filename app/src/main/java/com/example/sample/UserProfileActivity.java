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

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    private EditText nameEditText, phoneNumberEditText, vehicleTypeEditText, genderEditText, vehicleNumberEditText;
    private Button saveButton;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        vehicleTypeEditText = findViewById(R.id.vehicleTypeEditText);
        genderEditText = findViewById(R.id.genderEditText);
        vehicleNumberEditText = findViewById(R.id.vehicleNumberEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(view -> saveUserProfile());
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String vehicleType = vehicleTypeEditText.getText().toString().trim();
        String gender = genderEditText.getText().toString().trim();
        String vehicleNumber = vehicleNumberEditText.getText().toString().trim();

        if (!name.isEmpty() && !phoneNumber.isEmpty() && !vehicleType.isEmpty() && !gender.isEmpty() && !vehicleNumber.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Create a user profile map
                HashMap<String, Object> userProfile = new HashMap<>();
                userProfile.put("name", name);
                userProfile.put("phoneNumber", phoneNumber);
                userProfile.put("vehicleType", vehicleType);
                userProfile.put("gender", gender);
                userProfile.put("vehicleNumber", vehicleNumber);

                // Save user profile to Realtime Database
                usersRef.child(user.getUid()).setValue(userProfile)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();

                            // Proceed to Role Selection Activity
                            Intent intent = new Intent(UserProfileActivity.this, RoleSelectionActivity.class);
                            intent.putExtra("userName", name); // Pass user details for next step
                            intent.putExtra("userPhoneNumber", phoneNumber);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        } else {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }
    }
}
