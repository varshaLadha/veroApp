package com.example.lcom151_two.veroapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.adapters.ImageAdapter;

import java.security.Permission;

public class MainActivity extends BaseClass {

    Button login,signup;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String wantPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private Context context;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(sp.contains("verified")){
            Intent intent=new Intent(MainActivity.this,UserProfile.class);
            startActivity(intent);
            finish();
        }

        context = this;
        activity = MainActivity.this;

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

        if (!checkPermission(wantPermission)) {
            requestPermission(wantPermission);
        } else {
            //Toast.makeText(context,"Permission already granted.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void requestPermission(String permission){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
            //Toast.makeText(context, "Write external storage permission allows us to write data.Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Allow app to access phone storage")
                    .setTitle("Permission request");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    Log.i("grant", "Clicked");
                    makeRequest();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            makeRequest();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission Granted. Now you can write data.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context,"Permission Denied. You cannot write data.",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
