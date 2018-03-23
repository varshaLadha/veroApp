package com.example.lcom151_two.veroapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.lcom151_two.veroapp.fragments.ProfileFragment;
import com.example.lcom151_two.veroapp.fragments.NotificationFragment;
import com.example.lcom151_two.veroapp.fragments.PostsFragment;
import com.example.lcom151_two.veroapp.fragments.SearchFragment;
import com.example.lcom151_two.veroapp.fragments.SettingsFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserHome extends BaseClass implements View.OnClickListener{

    List<String> postText;
    ImageView background;
    android.support.v7.app.ActionBar ab;
    ImageButton posts,notification,settings,search,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

       initView();

    }

    public void initView(){
        posts=(ImageButton) findViewById(R.id.posts);
        notification=(ImageButton) findViewById(R.id.notifications);
        settings=(ImageButton) findViewById(R.id.settings);
        search=(ImageButton) findViewById(R.id.search);
        user=(ImageButton) findViewById(R.id.profile);

        postText=new ArrayList<String>();

        if(!sp.contains("userId")){
            Intent intent=new Intent(UserHome.this,Login.class);
            startActivity(intent);
            finish();
        }else {
            Gson gson=new Gson();
            String object=sp.getString("userDetail","");
            UserDataModelClass udm=gson.fromJson(object,UserDataModelClass.class);

            ab=getSupportActionBar();
            ab.setTitle(udm.displayName);
            Log.i("User info","UserId : "+udm.userId+" Display name : "+udm.displayName+" User Profile : "+udm.userProfile+" User name : "+udm.userName+" User Status : "+udm.status);

            background=(ImageView)findViewById(R.id.background);
            Bitmap bitmap=BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.background_splash));
            background.setImageBitmap(bitmap);

            loadFragment(new PostsFragment());
            posts.setImageResource(R.drawable.posts_focused);
        }
    }

    @Override
    public void onClick(View v)
    {
        Fragment fragment;
        switch (v.getId()){
            case R.id.posts:
                setFocused(R.drawable.posts_focused,R.drawable.search,R.drawable.ntoifications,R.drawable.settings,R.drawable.user1);

                    fragment=new PostsFragment();
                    loadFragment(fragment);
                    break;

                case R.id.search:
                    setFocused(R.drawable.posts,R.drawable.search_focused,R.drawable.ntoifications,R.drawable.settings,R.drawable.user1);

                    fragment=new SearchFragment();
                    loadFragment(fragment);
                    break;

                case R.id.notifications:
                    setFocused(R.drawable.posts,R.drawable.search,R.drawable.ntoifications_focused,R.drawable.settings,R.drawable.user1);

                    fragment=new NotificationFragment();
                    loadFragment(fragment);
                    break;

                case R.id.settings:
                    setFocused(R.drawable.posts,R.drawable.search,R.drawable.ntoifications,R.drawable.settings_focused,R.drawable.user1);

                    fragment=new SettingsFragment();
                    loadFragment(fragment);
                    break;

                case R.id.profile:
                    setFocused(R.drawable.posts,R.drawable.search,R.drawable.ntoifications,R.drawable.settings,R.drawable.user1_focused);

                    fragment=new ProfileFragment();
                    loadFragment(fragment);
                    break;
            }
    }

    public void setFocused(int postDrawable,int searchDrawable,int notificationDrawable,int settingDrawable,int profileDrawable){
        posts.setImageResource(postDrawable);
        search.setImageResource(searchDrawable);
        notification.setImageResource(notificationDrawable);
        settings.setImageResource(settingDrawable);
        user.setImageResource(profileDrawable);
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }
}
