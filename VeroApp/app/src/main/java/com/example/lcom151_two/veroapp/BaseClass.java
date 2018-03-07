package com.example.lcom151_two.veroapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseClass extends AppCompatActivity {

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface=ApiClient.getClient().create(ApiInterface.class);
    }
}
