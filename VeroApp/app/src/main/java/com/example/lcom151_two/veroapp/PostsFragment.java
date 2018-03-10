package com.example.lcom151_two.veroapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsFragment extends Fragment {

    GridView posts;
    ArrayList<String> postText;
    ArrayList<String> displayName;
    ArrayList<String> comments;
    ArrayList<String> likescnt;
    ArrayList<Integer> postId;
    SharedPreferences sp;
    ApiInterface apiInterface;
    postDisplayAdapter adapter;

    public PostsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_posts,container,false);
        sp=getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        posts=(GridView)view.findViewById(R.id.postsGrid);
        postText=new ArrayList<String>();
        displayName=new ArrayList<String>();
        comments=new ArrayList<String>();
        likescnt=new ArrayList<String>();
        postId=new ArrayList<Integer>();

        apiInterface=ApiClient.getClient().create(ApiInterface.class);

        String userId=sp.getString("userId","");

        Call<getPostsResponseModel> call = apiInterface.getPosts(userId);
        call.enqueue(new Callback<getPostsResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<getPostsResponseModel> call, Response<getPostsResponseModel> response) {
                if(response.code()==200){
                    List<Message> data=response.body().getRows();
                    List<List<Datum>> likes=response.body().getData();

                    for(int i=0;i<likes.size();i++){
                        Log.i("Loop iteration ",i+"");
                        if(i%2==0){
                            Log.i("Comments ",likes.get(i).get(0).getComments().toString());
                            comments.add(likes.get(i).get(0).getComments().toString());
                        }else if(i%2==1){
                            Log.i("Likes ",likes.get(i).get(0).getLikes().toString());
                            likescnt.add(likes.get(i).get(0).getLikes().toString());
                        }
                    }

                    for(int i=0;i<data.size();i++){
                        postText.add(data.get(i).getPostText());
                        displayName.add(data.get(i).getDisplayName());
                        postId.add(data.get(i).getPostId());
                    }
                    //Toast.makeText(getContext(), "Comments size "+comments.size()+" likes size "+likescnt.size(), Toast.LENGTH_SHORT).show();
                    adapter=new postDisplayAdapter(getContext(),displayName,postText,comments,likescnt,postId);
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
