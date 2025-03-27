package com.example.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchedUsersActivity extends AppCompatActivity {

    private RecyclerView matchedUsersRecyclerView;
    private TextView noMatchesTextView;
    private MatchedUsersAdapter matchedUsersAdapter;
    private List<User> matchedUsersList;
    private DatabaseReference destinationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_users);

        matchedUsersRecyclerView = findViewById(R.id.matchedUsersRecyclerView);
        noMatchesTextView = findViewById(R.id.noMatchesTextView);
        matchedUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        matchedUsersList = new ArrayList<>();
        matchedUsersAdapter = new MatchedUsersAdapter(matchedUsersList);
        matchedUsersRecyclerView.setAdapter(matchedUsersAdapter);

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        destinationRef = database.getReference("destinations");

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference("users").child(currentUserId);

        // Fetch user data
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    String destination = snapshot.child("destination").getValue(String.class);

                    if (role != null && destination != null) {
                        fetchMatchedUsers(role, destination, currentUserId);
                    } else {
                        Toast.makeText(MatchedUsersActivity.this, "Role or destination not found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MatchedUsersActivity.this, "User profile not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MatchedUsersActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMatchedUsers(String role, String destination, String currentUserId) {
        String matchRole = role.equals("Rider") ? "passengers" : "riders";

        destinationRef.child(destination).child(matchRole).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchedUsersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null && !snapshot.getKey().equals(currentUserId)) { // Exclude self
                        matchedUsersList.add(user);
                    }
                }

                matchedUsersAdapter.notifyDataSetChanged();

                if (matchedUsersList.isEmpty()) {
                    noMatchesTextView.setVisibility(View.VISIBLE);
                    matchedUsersRecyclerView.setVisibility(View.GONE);
                } else {
                    noMatchesTextView.setVisibility(View.GONE);
                    matchedUsersRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MatchedUsersActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
