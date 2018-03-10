package com.example.lcom151_two.veroapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class postDisplayAdapter extends BaseAdapter{

    private Context context;
    ArrayList<String> userName;
    ArrayList<String> postText;
    ArrayList<String> cmtcnt;
    ArrayList<String> lkscnt;
    ArrayList<Integer> postId;
    SharedPreferences sp;
    String userId;

    public postDisplayAdapter(Context context,ArrayList<String> userName,ArrayList<String> postText,ArrayList<String> cmtcnt,ArrayList<String> lkscnt,ArrayList<Integer> postId){
        this.context=context;
        this.userName=userName;
        this.postText=postText;
        this.cmtcnt=cmtcnt;
        this.lkscnt=lkscnt;
        this.postId=postId;
        sp=context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        userId=sp.getString("userId","");
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
                    Toast.makeText(context, userId, Toast.LENGTH_SHORT).show();
                    likescnt.setText((Integer.parseInt((String) likescnt.getText())+1)+"");
                    likedpost.setVisibility(View.VISIBLE);
                    likePost.setVisibility(View.INVISIBLE);
                    //Toast.makeText(context, likescnt.getText(), Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }catch (Exception e){
            Toast.makeText(context, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return convertView;
    }
}
