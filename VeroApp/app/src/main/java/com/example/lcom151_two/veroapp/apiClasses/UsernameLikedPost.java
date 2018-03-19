package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsernameLikedPost {

    @SerializedName("displayName")
    @Expose
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
