package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchUserResponseModel {
    @SerializedName("message")
    @Expose
    private List<SearchDataModel> message = null;

    public List<SearchDataModel> getMessage() {
        return message;
    }

    public void setMessage(List<SearchDataModel> message) {
        this.message = message;
    }

}
