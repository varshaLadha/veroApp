package com.example.lcom151_two.veroapp.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lcom151_two.veroapp.LocalDatabase.DatabaseHandler;
import com.example.lcom151_two.veroapp.LocalDatabase.FaviouratePosts;
import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.adapters.FavouratePostsAdapter;
import com.example.lcom151_two.veroapp.adapters.PostsDisplayAdapter1;
import com.example.lcom151_two.veroapp.ModalClasses.PostsModelClass;

import java.util.ArrayList;
import java.util.List;

public class FavouratePosts extends Fragment {

    ArrayList<String> list;
    DatabaseHandler db;
    ArrayList<PostsModelClass> postsModelClass;
    RecyclerView postView;
    FavouratePostsAdapter adapter;
    SharedPreferences sp;
    String userId;

    Activity context;
    public FavouratePosts() {

        postsModelClass= new ArrayList<PostsModelClass>();
        list=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_favourateposts, container, false);
        context=getActivity();
        db=new DatabaseHandler(context);
        postView=view.findViewById(R.id.faviouratePosts);

        List<FaviouratePosts> posts=db.getAllPosts();

        if(posts.size()==0){
            TextView noPosts=view.findViewById(R.id.noPosts);
            noPosts.setVisibility(View.VISIBLE);
            noPosts.setText("No favourate posts yet!!");
        }else {

            for (int i = 0; i < posts.size(); i++) {
                list.add(posts.get(i).getDisplayName());
                postsModelClass.add(new PostsModelClass(posts.get(i).getPostText(), posts.get(i).getPostTime(), posts.get(i).getPostPic(), posts.get(i).getUserProfile(), posts.get(i).getDisplayName(), posts.get(i).getComments(), posts.get(i).getLikescnt(), posts.get(i).getPostId()));
            }

            adapter = new FavouratePostsAdapter(getContext(), postsModelClass);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            postView.setLayoutManager(layoutManager);
            postView.setAdapter(adapter);
        }

        return view;
    }

}
