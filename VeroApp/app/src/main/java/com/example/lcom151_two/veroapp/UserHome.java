package com.example.lcom151_two.veroapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
        BottomNavigationView navigation=(BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new PostsFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()){
                case R.id.posts:
                    fragment=new PostsFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.search:
                    fragment=new SearchFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.notifications:
                    fragment=new NotificationFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.settings:
                    fragment=new SettingsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }
}
