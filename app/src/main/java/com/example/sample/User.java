package com.example.sample;

public class User {
    private String username;
    private String role;
    private String destination;

    public User() {
        // Default constructor for Firebase
    }

    public User(String username, String role, String destination) {
        this.username = username;
        this.role = role;
        this.destination = destination;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
