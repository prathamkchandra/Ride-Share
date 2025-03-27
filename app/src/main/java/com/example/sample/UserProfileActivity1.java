package com.example.sample;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity1 extends AppCompatActivity {
    private TextView nameTextView, phoneNumberTextView, startingPointTextView;
    private ImageView profileImageView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        nameTextView = findViewById(R.id.nameTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        startingPointTextView = findViewById(R.id.startingPointTextView);
        profileImageView = findViewById(R.id.profileImageView);

        db = FirebaseFirestore.getInstance();

        // Get userId from the intent
        String userId = getIntent().getStringExtra("userId");
        fetchUserDetails(userId);
    }

    private void fetchUserDetails(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Populate user details
                        String name = documentSnapshot.getString("name");
                        String phoneNumber = documentSnapshot.getString("phoneNumber");
                        String startingPoint = documentSnapshot.getString("startingPoint");
                        String profilePictureUrl = documentSnapshot.getString("profilePictureUrl");

                        nameTextView.setText(name != null && !name.isEmpty() ? name : "Anonymous");
                        phoneNumberTextView.setText(phoneNumber != null && !phoneNumber.isEmpty() ? phoneNumber : "Not Provided");
                        startingPointTextView.setText(startingPoint != null ? startingPoint : "Unknown");

                        Glide.with(this)
                                .load(profilePictureUrl.equals("No Profile Picture")
                                        ? R.drawable.default_avatar_background
                                        : profilePictureUrl)
                                .into(profileImageView);
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
