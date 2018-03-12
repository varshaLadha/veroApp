package com.example.lcom151_two.veroapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.lcom151_two.veroapp.fragments.NotificationFragment;
import com.example.lcom151_two.veroapp.fragments.PostsFragment;
import com.example.lcom151_two.veroapp.fragments.SearchFragment;
import com.example.lcom151_two.veroapp.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class UserHome extends BaseClass {

    List<String> postText;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        postText=new ArrayList<String>();

        if(!sp.contains("userId")){
            Intent intent=new Intent(UserHome.this,Login.class);
            startActivity(intent);
            finish();
        }else {
            background=(ImageView)findViewById(R.id.background);
            Bitmap bitmap=BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.background_splash));
            background.setImageBitmap(bitmap);

            BottomNavigationView navigation=(BottomNavigationView)findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            loadFragment(new PostsFragment());
        }
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
