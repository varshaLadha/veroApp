package com.example.lcom151_two.veroapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class getPostsResponseModel {
//    @SerializedName("status")
//    @Expose
//    private Integer status;
//    @SerializedName("message")
//    @Expose
//    private List<Message> message = null;
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public List<Message> getMessage() {
//        return message;
//    }
//
//    public void setMessage(List<Message> message) {
//        this.message = message;
//    }
    @SerializedName("data")
    @Expose
    private List<List<Datum>> data = null;
    @SerializedName("rows")
    @Expose
    private List<Message> Message = null;

    public List<List<Datum>> getData() {
        return data;
    }

    public void setData(List<List<Datum>> data) {
        this.data = data;
    }

    public List<Message> getRows() {
        return Message;
    }

    public void setRows(List<Message> Message) {
        this.Message = Message;
    }

}
