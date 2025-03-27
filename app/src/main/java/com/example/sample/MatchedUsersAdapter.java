package com.example.sample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchedUsersAdapter extends RecyclerView.Adapter<MatchedUsersAdapter.MatchedUserViewHolder> {

    private List<User> matchedUsersList;

    public MatchedUsersAdapter(List<User> matchedUsersList) {
        this.matchedUsersList = matchedUsersList;
    }

    @Override
    public MatchedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matched_user, parent, false);
        return new MatchedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchedUserViewHolder holder, int position) {
        User user = matchedUsersList.get(position);
        holder.usernameTextView.setText(user.getUsername());
        holder.roleTextView.setText(user.getRole());
        holder.destinationTextView.setText(user.getDestination());
    }

    @Override
    public int getItemCount() {
        return matchedUsersList.size();
    }

    public static class MatchedUserViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView, roleTextView, destinationTextView;

        public MatchedUserViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
        }
    }
}
