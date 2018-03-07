package com.example.lcom151_two.veroapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class UserHome extends BaseClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        if(getIntent().getExtras()==null){
            Intent intent=new Intent(UserHome.this,Login.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent=getIntent();
            String userId=intent.getStringExtra("userId");
            retrofit2.Call<getPostsResponseModel> call=apiInterface.getPosts(userId);
            call.enqueue(new Callback<getPostsResponseModel>() {
                @Override
                public void onResponse(retrofit2.Call<getPostsResponseModel> call, Response<getPostsResponseModel> response) {
                    if(response.body().getStatus()==1){
                        List<Message> data=response.body().getMessage();
                        //Log.i("data", String.valueOf(data.get));
                        for(int i=0;i<data.size();i++){
                            Log.i("Data ",data.get(i).getPostText());
                        }
                    }
                    else {
                        Toast.makeText(UserHome.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<getPostsResponseModel> call, Throwable t) {
                    Toast.makeText(UserHome.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
