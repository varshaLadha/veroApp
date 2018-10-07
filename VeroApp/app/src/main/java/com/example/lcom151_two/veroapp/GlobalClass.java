package com.example.lcom151_two.veroapp;

import android.app.Application;

import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;

public class GlobalClass extends Application {

    public static String profileurl,posturl;
    public static ApiInterface apiInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        profileurl="http://192.168.200.147:3005/profile/";
        posturl="http://192.168.200.147:3005/post/";
        apiInterface= ApiClient.getClient().create(ApiInterface.class);
    }

}
