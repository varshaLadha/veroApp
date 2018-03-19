package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private List<CommentResponseData> message = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CommentResponseData> getMessage() {
        return message;
    }

    public void setMessage(List<CommentResponseData> message) {
        this.message = message;
    }
}
