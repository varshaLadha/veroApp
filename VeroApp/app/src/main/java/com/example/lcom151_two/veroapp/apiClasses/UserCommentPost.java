package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserCommentPost {

    @SerializedName("message")
    @Expose
    private List<UsernameCommentPost> message = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<UsernameCommentPost> getMessage() {
        return message;
    }

    public void setMessage(List<UsernameCommentPost> message) {
        this.message = message;
    }

}
