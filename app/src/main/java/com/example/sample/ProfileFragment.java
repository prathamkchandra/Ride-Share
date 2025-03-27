package com.example.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bumptech.glide.Glide;  // For loading the profile picture

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get references to the UI elements
        ImageView profileImage = rootView.findViewById(R.id.profileImage);
        TextView nameTextView = rootView.findViewById(R.id.nameTextView);
        TextView emailTextView = rootView.findViewById(R.id.emailTextView);
        Button editProfileButton = rootView.findViewById(R.id.editProfileButton);

        // Get the current Firebase user
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Set the user's name and email dynamically
            String displayName = user.getDisplayName();
            String email = user.getEmail();

            nameTextView.setText(displayName != null ? displayName : "No Name Available");
            emailTextView.setText(email != null ? email : "No Email Available");

            // Load the user's profile image if available (using Glide or Picasso)
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.ic_profile_placeholder)  // Placeholder image
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.ic_profile_placeholder);  // Default image
            }
        } else {
            // If user is not logged in, show a message
            Toast.makeText(getActivity(), "No user found. Please log in again.", Toast.LENGTH_SHORT).show();
        }

        // Set an onClickListener for the edit profile button
        editProfileButton.setOnClickListener(v -> {
            // Handle profile editing here
            // For example, you can start a new Activity or Fragment to allow the user to edit their profile
            // For example, navigating to EditProfileActivity:
            // Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            // startActivity(intent);
        });

        return rootView;
    }
}
