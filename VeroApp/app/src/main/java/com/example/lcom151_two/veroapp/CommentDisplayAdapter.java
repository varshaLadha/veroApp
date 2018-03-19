package com.example.lcom151_two.veroapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentDisplayAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> names,commentText;

    public CommentDisplayAdapter(Context context,ArrayList<String> names,ArrayList<String> commentText){
        this.context=context;
        this.names=names;
        this.commentText=commentText;
    }

    @Override
    public int getCount() {
        return names.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.commentscontent,null);
        TextView username,commentsText;
        final EditText comment;

        username=convertView.findViewById(R.id.username);
        commentsText=convertView.findViewById(R.id.commentText);

        try{
            username.setText(names.get(position));
            commentsText.setText(commentText.get(position));

            return convertView;
        }catch (Exception e){
            Toast.makeText(context, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return convertView;
    }
}
