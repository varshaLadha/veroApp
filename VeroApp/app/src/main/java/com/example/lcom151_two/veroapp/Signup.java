package com.example.lcom151_two.veroapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Signup extends BaseClass {

    TextView countrycode;
    EditText userid,name,email1;
    Button register,loginRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        countrycode = (TextView)findViewById(R.id.countrycode);
        userid = (EditText)findViewById(R.id.userid);
        name = (EditText)findViewById(R.id.name);
        email1 = (EditText)findViewById(R.id.email);
        register = (Button)findViewById(R.id.register);
        loginRedirect=(Button)findViewById(R.id.loginRedirect);

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Signup.this,Login.class);
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userid.getText().toString();
                String displayName = name.getText().toString();
                String email = email1.getText().toString();

                if(TextUtils.isEmpty(userId) || TextUtils.isEmpty(displayName) || TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(userId,email,displayName);
                }
            }
        });
    }

    public void registerUser(String userId,String email,String displayName){
        userId="91"+userId;
        Log.d("Data",userId+" "+email+" "+displayName);
    }
}
