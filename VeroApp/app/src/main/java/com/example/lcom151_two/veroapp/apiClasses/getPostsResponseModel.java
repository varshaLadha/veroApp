package com.example.lcom151_two.veroapp.apiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class getPostsResponseModel {
    @SerializedName("data")
    @Expose
    private List<List<Datum>> data = null;
    @SerializedName("rows")
    @Expose
    private List<com.example.lcom151_two.veroapp.apiClasses.Message> Message = null;

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
