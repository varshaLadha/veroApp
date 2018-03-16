package com.example.lcom151_two.veroapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.Datum;
import com.example.lcom151_two.veroapp.apiClasses.Message;
import com.example.lcom151_two.veroapp.apiClasses.getPostsResponseModel;
import com.example.lcom151_two.veroapp.postDisplayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsFragment extends Fragment {

    GridView posts;
    ArrayList<String> postText,postTime,postPic,userProfile;
    ArrayList<String> displayName;
    ArrayList<String> comments;
    ArrayList<String> likescnt;
    ArrayList<Integer> postId;
    SharedPreferences sp;
    ApiInterface apiInterface;
    postDisplayAdapter adapter;
    FloatingActionButton fab;
    String url,url1;
    View view1;
    PopupWindow popupWindow;
    android.support.design.widget.CoordinatorLayout coordinatorLayout;

    public PostsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_posts,container,false);
        sp=getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        posts=(GridView)view.findViewById(R.id.postsGrid);
        coordinatorLayout=view.findViewById(R.id.coordinatelayout);
        postText=new ArrayList<String>();
        displayName=new ArrayList<String>();
        postTime=new ArrayList<String>();
        comments=new ArrayList<String>();
        likescnt=new ArrayList<String>();
        postId=new ArrayList<Integer>();
        postPic=new ArrayList<String>();
        userProfile=new ArrayList<String>();
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        url="http://192.168.200.147:3005/post/";
        url1="http://192.168.200.147:3005/profile/";

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater1=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view1=inflater1.inflate(R.layout.posts,null);
                popupWindow=new PopupWindow(view1,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.showAtLocation(coordinatorLayout, Gravity.CENTER,0,0);
                //Toast.makeText(getContext(), "Fab clicked", Toast.LENGTH_SHORT).show();
            }
        });

        apiInterface= ApiClient.getClient().create(ApiInterface.class);

        String userId=sp.getString("userId","");

        Call<getPostsResponseModel> call = apiInterface.getPosts(userId);
        call.enqueue(new Callback<getPostsResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<getPostsResponseModel> call, Response<getPostsResponseModel> response) {
                if(response.code()==200){
                    List<Message> data=response.body().getRows();
                    List<List<Datum>> likes=response.body().getData();

                    for(int i=0;i<likes.size();i++){
                        if(i%2==0){
                            comments.add(likes.get(i).get(0).getComments().toString());
                        }else if(i%2==1){
                            likescnt.add(likes.get(i).get(0).getLikes().toString());
                        }
                    }

                    for(int i=0;i<data.size();i++){
                        postText.add(data.get(i).getPostText());
                        displayName.add(data.get(i).getDisplayName());
                        postId.add(data.get(i).getPostId());
                        postTime.add(data.get(i).getCreatedAt());
                        userProfile.add(data.get(i).getUserProfilePhoto());
                        if(TextUtils.isEmpty(data.get(i).getPostUrl())){
                           postPic.add(data.get(i).getPostUrl());
                        }else {
                            postPic.add(url+data.get(i).getPostUrl());
                        }
                    }
                    Log.i("User profile pic",postPic.toString());
                    adapter=new postDisplayAdapter(getContext(),displayName,postText,comments,likescnt,postId,postTime,postPic);
                    posts.setAdapter(adapter);
                }
                else {
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<getPostsResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
