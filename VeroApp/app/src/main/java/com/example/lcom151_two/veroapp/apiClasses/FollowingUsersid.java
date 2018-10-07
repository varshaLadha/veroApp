package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowingUsersid {
    @SerializedName("fuserId")
    @Expose
    private String fuserId;

    public String getFuserId() {
        return fuserId;
    }

    public void setFuserId(String fuserId) {
        this.fuserId = fuserId;
    }
}
