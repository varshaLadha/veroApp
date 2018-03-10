package com.example.lcom151_two.veroapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseClass extends AppCompatActivity {

    ApiInterface apiInterface;
    Bitmap bitmap;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface=ApiClient.getClient().create(ApiInterface.class);

        sp=getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor=sp.edit();
    }
}
