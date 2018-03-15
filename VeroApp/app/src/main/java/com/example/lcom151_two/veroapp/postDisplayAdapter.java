package com.example.lcom151_two.veroapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;

import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.postLikeResponseModel;
import com.squareup.picasso.Picasso;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Callback;
import retrofit2.Response;

public class postDisplayAdapter extends BaseAdapter{

    private Context context;
    ArrayList<String> userName,postText,cmtcnt,lkscnt,postTime,posturl,userProfile;
    ArrayList<Integer> postId;
    SharedPreferences sp;
    String userId;
    ApiInterface apiInterface;
    int height,width;

    public postDisplayAdapter(Context context,ArrayList<String> userName,ArrayList<String> postText,ArrayList<String> cmtcnt,ArrayList<String> lkscnt,ArrayList<Integer> postId,ArrayList<String> postTime,ArrayList<String> posturl){
        this.context=context;
        this.userName=userName;
        this.postText=postText;
        this.cmtcnt=cmtcnt;
        this.lkscnt=lkscnt;
        this.postId=postId;
        this.postTime=postTime;
        this.posturl=posturl;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView displayName,postContent,commentcnt,likescnt,postsTime;
        final ImageView likePost,likedpost,postPic;
        try {
            convertView= LayoutInflater.from(context).inflate(R.layout.postslayout,null);

            DisplayMetrics metrics=context.getResources().getDisplayMetrics();
            height=metrics.heightPixels;
            width=metrics.widthPixels;

            //Toast.makeText(context, height+" "+width , Toast.LENGTH_SHORT).show();

            displayName=convertView.findViewById(R.id.username);
            postContent=convertView.findViewById(R.id.postText);
            commentcnt=convertView.findViewById(R.id.commentcnt);
            likescnt=convertView.findViewById(R.id.likescnt);
            likePost=convertView.findViewById(R.id.likepost);
            likedpost=convertView.findViewById(R.id.likedpost);
            postsTime=convertView.findViewById(R.id.postTime);
            postPic=convertView.findViewById(R.id.postpic);

            displayName.setText(userName.get(position));
            if(postText.get(position)==""){
                postContent.setVisibility(View.GONE);
            }else {
                postContent.setText(postText.get(position));
            }

            commentcnt.setText(cmtcnt.get(position));
            likescnt.setText(lkscnt.get(position));

            if(!TextUtils.isEmpty(posturl.get(position))){

                Picasso.get()
                        .load(posturl.get(position))
                        .resize(width,height*2/5)
                        .into(postPic);
            }


            ZonedDateTime parsed = ZonedDateTime.parse(postTime.get(position));
            ZonedDateTime z = parsed.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

            DateTimeFormatter fmt;
            if(DateFormat.is24HourFormat(context)){
                fmt = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
            }else {
                fmt = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm:ss a", Locale.ENGLISH);
            }
            postsTime.setText(fmt.format(z));

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
