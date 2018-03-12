package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponseModel {

    @SerializedName("message")
    @Expose
    private List<searchData> message = null;

    public List<searchData> getMessage() {
        return message;
    }

    public void setMessage(List<searchData> message) {
        this.message = message;
    }
}
