package com.example.lcom151_two.veroapp;

public class UserDataModelClass {

    String userId;
    String userName;
    String userProfile;
    String status;
    String email;
    String displayName;

    public UserDataModelClass(String userId, String userName, String userProfile, String status, String email, String displayName){
        this.userId=userId;
        this.userName=userName;
        this.userProfile=userProfile;
        this.status=status;
        this.email=email;
        this.displayName=displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setStatus(String status) { this.status = status; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public void setEmail(String email) { this.email = email; }

    public void setUserId(String userId) { this.userId = userId; }

    public void setUserName(String userName) { this.userName = userName; }

    public void setUserProfile(String userProfile) { this.userProfile = userProfile; }
}
