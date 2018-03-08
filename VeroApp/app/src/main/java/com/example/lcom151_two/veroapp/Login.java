package com.example.lcom151_two.veroapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends BaseClass {

    EditText mobile;
    Button otp,signupRedirect;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobile=(EditText)findViewById(R.id.mobile);
        otp=(Button)findViewById(R.id.otp);
        background=(ImageView)findViewById(R.id.background);

        Bitmap bitmap=BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.background_splash));
        background.setImageBitmap(bitmap);
        //signupRedirect=(Button)findViewById(R.id.signupRedirect);

//        signupRedirect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Login.this,Signup.class);
//                startActivity(i);
//                finish();
//            }
//        });

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId=mobile.getText().toString();
                if(TextUtils.isEmpty(userId)){
                    Toast.makeText(Login.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                }else {
                    login(userId);
                }
            }
        });
    }

    public void login(String userId){
        userId="91"+userId;
        Call<responseModel> call = apiInterface.userLogin(userId);
        final String finalUserId = userId;
        call.enqueue(new Callback<responseModel>() {
            @Override
            public void onResponse(Call<responseModel> call, Response<responseModel> response) {
                responseModel model=response.body();
                if(model.getStatus()== 1){
                    Intent intent = new Intent(Login.this,Otp.class);
                    intent.putExtra("otpValue",model.getMessage()+"");
                    intent.putExtra("userId", finalUserId);
                    startActivity(intent);
                    //Toast.makeText(Login.this, model.getMessage()+"valid mobile number", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Login.this, model.getMessage()+" invalid number "+model.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<responseModel> call, Throwable t) {
                Toast.makeText(Login.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error ",t.getMessage());
            }
        });
    }
}
