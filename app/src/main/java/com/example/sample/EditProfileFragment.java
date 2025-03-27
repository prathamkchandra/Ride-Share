package com.example.sample; // Use your app's package name

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private EditText nameEditText, phoneEditText;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initialize UI components
        profileImageView = rootView.findViewById(R.id.editProfileImage);
        nameEditText = rootView.findViewById(R.id.editNameEditText);
        Button changeProfileImageButton = rootView.findViewById(R.id.changeProfileImageButton);
        Button saveProfileButton = rootView.findViewById(R.id.saveProfileButton);

        // Set the text from the string resources
        changeProfileImageButton.setText(getString(R.string.change_profile_picture)); // Using string resource
        saveProfileButton.setText(getString(R.string.save_changes)); // Using string resource
        nameEditText.setHint(getString(R.string.enter_name)); // Using string resource
        phoneEditText.setHint(getString(R.string.enter_phone_number)); // Using string resource

        // Set up the change profile picture button
        changeProfileImageButton.setOnClickListener(v -> openImageChooser());

        // Set up the save button
        saveProfileButton.setOnClickListener(v -> saveProfile());

        return rootView;
    }

    // Opens the image picker to change the profile picture
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image picker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);  // Set the selected image to the ImageView
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Save the user's changes (for simplicity, we'll just print the data for now)
    private void saveProfile() {
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        // Save the data to a database, SharedPreferences, or an API as needed
        // For now, we'll just print the data
        System.out.println("Saved Profile - Name: " + name + ", Phone: " + phone);

        // You can show a message here to confirm that changes have been saved
    }
}
