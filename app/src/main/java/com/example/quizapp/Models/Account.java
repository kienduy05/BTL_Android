package com.example.quizapp.Models;

public class Account {

    private int userId;
    private String fullName;
    private String email;
    private String password;
    private int role;
    private String avatarUrl;
    private String createdAt;


    public Account() {}

    public Account(int userId, String fullName, String email, String password,
                   int role, String avatarUrl, String createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }

    public Account(String fullName, String email, String password, int role, String avatarUrl, String createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}