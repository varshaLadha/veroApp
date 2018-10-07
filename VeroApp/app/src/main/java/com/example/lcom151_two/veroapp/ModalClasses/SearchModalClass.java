package com.example.lcom151_two.veroapp.ModalClasses;

public class SearchModalClass {

    String userId,userProfile,displayName,email;

    public SearchModalClass(){}

    public SearchModalClass(String userId,String userProfile,String displayName,String email){
        this.userId=userId;
        this.userProfile=userProfile;
        this.displayName=displayName;
        this.email=email;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
