package com.example.lcom151_two.veroapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Callback;
import retrofit2.Response;

public class Otp extends BaseClass {

    EditText otpinput;
    Button verify;
    int otpValue;
    String userId;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        background=(ImageView)findViewById(R.id.background);
        Bitmap bitmap=BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.background_splash));
        background.setImageBitmap(bitmap);

        if(getIntent().getExtras()==null){
            Toast.makeText(this, "No otp found", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent=getIntent();
            otpValue= Integer.parseInt(intent.getStringExtra("otpValue"));
            userId=intent.getStringExtra("userId");
            Toast.makeText(this, "OTP value "+otpValue+" userId "+userId, Toast.LENGTH_LONG).show();
        }

        otpinput = (EditText)findViewById(R.id.otpinput);
        verify = (Button)findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(otpinput.getText().toString())){
                    Toast.makeText(Otp.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(Otp.this, Integer.parseInt(otpinput.getText().toString())+"", Toast.LENGTH_SHORT).show();
                    verifyUser(Integer.parseInt(otpinput.getText().toString()));

                }
            }
        });
    }

    public void verifyUser(int otp){
        retrofit2.Call<responseModel> call = apiInterface.verifyUser(otp,userId);
        call.enqueue(new Callback<responseModel>() {
            @Override
            public void onResponse(retrofit2.Call<responseModel> call, Response<responseModel> response) {
                responseModel rmodel=response.body();
                if(rmodel.getStatus()==1){
                    //Toast.makeText(Otp.this, rmodel.getMessage()+"Valid otp", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Otp.this,userProfile.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }else {
                    Toast.makeText(Otp.this, rmodel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<responseModel> call, Throwable t) {
                Toast.makeText(Otp.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
