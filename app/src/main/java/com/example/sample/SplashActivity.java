package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth; // Import FirebaseAuth

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Use a handler to delay the transition to the next activity
        new Handler().postDelayed(() -> {
            // After the delay, check if the user is logged in
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                // User is logged in, go to UserProfileActivity
                startActivity(new Intent(SplashActivity.this, UserProfileActivity.class));
            } else {
                // User is not logged in, go to MainActivity (login)
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish(); // Close this activity
        }, 2000); // 2 seconds delay
    }
}
