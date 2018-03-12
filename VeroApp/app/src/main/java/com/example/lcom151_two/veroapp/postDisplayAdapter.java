package com.example.lcom151_two.veroapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.postLikeResponseModel;
import com.example.lcom151_two.veroapp.apiClasses.responseModel;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;

public class postDisplayAdapter extends BaseAdapter{

    private Context context;
    ArrayList<String> userName;
    ArrayList<String> postText;
    ArrayList<String> cmtcnt;
    ArrayList<String> lkscnt;
    ArrayList<Integer> postId;
    SharedPreferences sp;
    String userId;
    ApiInterface apiInterface;

    public postDisplayAdapter(Context context,ArrayList<String> userName,ArrayList<String> postText,ArrayList<String> cmtcnt,ArrayList<String> lkscnt,ArrayList<Integer> postId){
        this.context=context;
        this.userName=userName;
        this.postText=postText;
        this.cmtcnt=cmtcnt;
        this.lkscnt=lkscnt;
        this.postId=postId;
        sp=context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        userId=sp.getString("userId","");
        apiInterface= ApiClient.getClient().create(ApiInterface.class);
    }
    @Override
    public int getCount() {
        return userName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView displayName,postContent,commentcnt,likescnt;
        final ImageView likePost,likedpost;
        try {
            convertView= LayoutInflater.from(context).inflate(R.layout.postslayout,null);
            displayName=convertView.findViewById(R.id.username);
            postContent=convertView.findViewById(R.id.postText);
            commentcnt=convertView.findViewById(R.id.commentcnt);
            likescnt=convertView.findViewById(R.id.likescnt);
            likePost=convertView.findViewById(R.id.likepost);
            likedpost=convertView.findViewById(R.id.likedpost);

            displayName.setText(userName.get(position));
            postContent.setText(postText.get(position));
            commentcnt.setText(cmtcnt.get(position));
            likescnt.setText(lkscnt.get(position));

            likePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likescnt.setText((Integer.parseInt((String) likescnt.getText())+1)+"");
                    likedpost.setVisibility(View.VISIBLE);
                    likePost.setVisibility(View.INVISIBLE);

                    retrofit2.Call<postLikeResponseModel> call=apiInterface.likePost(postId.get(position),userId);
                    call.enqueue(new Callback<postLikeResponseModel>() {
                        @Override
                        public void onResponse(retrofit2.Call<postLikeResponseModel> call, Response<postLikeResponseModel> response) {
                            if(response.code()==200){
                                Toast.makeText(context, "Post liked successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<postLikeResponseModel> call, Throwable t) {
                            Toast.makeText(context, "Problem liking post : "+t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("Error liking post : ",t.getMessage());
                        }
                    });
                }
            });

            return convertView;
        }catch (Exception e){
            Toast.makeText(context, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return convertView;
    }
}
