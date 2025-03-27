package com.example.sample;

public class UserProfile {
    private String uid;
    private String name;
    private String email;
    private String role;
    private String destination;

    public UserProfile() {
        // Default constructor for Firebase
    }

    // Constructor with four parameters
    public UserProfile(String uid, String name, String email, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.role = role;
        this.destination = null;  // Set destination to null initially
    }

    // Getters and setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
