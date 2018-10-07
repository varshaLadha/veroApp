package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class userProfileResponse {

    @SerializedName("userRegister")
    @Expose
    private List<userRegister> message = null;

    public List<userRegister> getMessage() {
        return message;
    }

    public void setMessage(List<userRegister> message) {
        this.message = message;
    }
}
