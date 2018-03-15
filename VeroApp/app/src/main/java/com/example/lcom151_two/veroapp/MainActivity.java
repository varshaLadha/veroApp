package com.example.lcom151_two.veroapp;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseClass {

    Button login,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(sp.contains("verified")){
            Intent intent=new Intent(MainActivity.this,UserProfile.class);
            startActivity(intent);
            finish();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        login=(Button)findViewById(R.id.login);
        //signup=(Button)findViewById(R.id.signup);

//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this,Signup.class);
//                startActivity(i);
//            }
//        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Login.class);
                startActivity(i);
            }
        });

    }
}
