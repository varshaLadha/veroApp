package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class postLikeResponseModel {

    @SerializedName("message")
    @Expose
    private LikeData likeData;

    public LikeData getData() {
        return likeData;
    }

    public void setMessage(LikeData likeData) {
        this.likeData = likeData;
    }
}
