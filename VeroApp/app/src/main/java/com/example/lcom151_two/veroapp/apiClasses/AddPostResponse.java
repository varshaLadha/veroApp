package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPostResponse {

    @SerializedName("message")
    @Expose
    private AddPostResponseData message;

    public AddPostResponseData getMessage() {
        return message;
    }

    public void setMessage(AddPostResponseData message) {
        this.message = message;
    }

}
