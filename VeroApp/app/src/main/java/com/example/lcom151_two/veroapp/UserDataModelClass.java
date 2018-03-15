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

}
