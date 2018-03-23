package com.example.lcom151_two.veroapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.GlobalClass;
import com.example.lcom151_two.veroapp.LocalDatabase.DatabaseHandler;
import com.example.lcom151_two.veroapp.LocalDatabase.FaviouratePosts;
import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.apiClasses.LikedPostsid;
import com.example.lcom151_two.veroapp.apiClasses.PostsLikedResponseModel;
import com.example.lcom151_two.veroapp.apiClasses.PostsModelClass;
import com.example.lcom151_two.veroapp.fragments.FavouratePosts;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavouratePostsAdapter extends RecyclerView.Adapter<FavouratePostsAdapter.MyViewHolder> {

    ArrayList<Integer> likedPostid,faviouratesPostid;
    SharedPreferences sp;
    String userId;
    int height, width;
    Context context;
    ArrayList<PostsModelClass> postsData;
    PostsModelClass postsModelClass;
    DatabaseHandler db;

    public FavouratePostsAdapter(Context context, ArrayList<PostsModelClass> postsData)
    {
        this.context = context;
        this.postsData=postsData;
        postsModelClass=new PostsModelClass();

        likedPostid = new ArrayList<Integer>();
        faviouratesPostid=new ArrayList<Integer>();
        sp = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        userId = sp.getString("userId", "");
        db=new DatabaseHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView displayName, postContent, commentcnt, likescnt, postsTime;
        public ImageView likePost, comment, postPic, addToFav, favPost, userpic;
        public MyViewHolder(View itemView) {
            super(itemView);

            displayName = itemView.findViewById(R.id.username);
            postContent = itemView.findViewById(R.id.postText);
            commentcnt = itemView.findViewById(R.id.commentcnt);
            likescnt = itemView.findViewById(R.id.likescnt);
            likePost = itemView.findViewById(R.id.like);
            postsTime = itemView.findViewById(R.id.postTime);
            postPic = itemView.findViewById(R.id.postpic);
            comment = itemView.findViewById(R.id.comment);
            userpic = itemView.findViewById(R.id.userpic);
            addToFav = itemView.findViewById(R.id.addToFaviourate);
            favPost=itemView.findViewById(R.id.faviouratePost);
            addToFav.setImageResource(R.drawable.ic_action_star_0);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.postslayout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        displayData(position,holder);

        Call<PostsLikedResponseModel> call = GlobalClass.apiInterface.postsLiked(userId);
        call.enqueue(new Callback<PostsLikedResponseModel>() {
            @Override
            public void onResponse(Call<PostsLikedResponseModel> call, Response<PostsLikedResponseModel> response) {
                if (response.body().getStatus() == 1) {
                    List<LikedPostsid> data = response.body().getData();
                    for (int i = 0; i < data.size(); i++) {
                        likedPostid.add(data.get(i).getPostId());
                    }

                    for (int i = 0; i < likedPostid.size(); i++) {
                        if (likedPostid.get(i) == postsData.get(position).getPostId()) {
                            holder.likePost.setImageResource(R.drawable.ic_action_likedheart);
                            holder.likePost.setClickable(false);
                            return;
                        } else {
                            holder.likePost.setImageResource(R.drawable.ic_action_heart);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PostsLikedResponseModel> call, Throwable t) {

            }
        });

        holder.favPost.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return postsData.size();
    }

    public void displayData(int position,MyViewHolder holder)
    {
        postsModelClass=postsData.get(position);
        if (!TextUtils.isEmpty(postsData.get(position).getUserProfile())) {
            Picasso.get()
                    .load(GlobalClass.profileurl + postsData.get(position).getUserProfile())
                    .into(holder.userpic);
        } else {
            holder.userpic.setImageResource(R.drawable.user);
        }

        holder.displayName.setText(postsData.get(position).getDisplayName());
        if (postsData.get(position).getPostText() == "") {
            holder.postContent.setVisibility(View.GONE);
        } else {
            holder.postContent.setText(postsData.get(position).getPostText());
        }

        holder.commentcnt.setText(postsData.get(position).getComments()+"");
        holder.likescnt.setText(postsData.get(position).getLikescnt()+"");

        if (!TextUtils.isEmpty(postsData.get(position).getPostPic())) {

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            height = metrics.heightPixels;
            width = metrics.widthPixels;

            Picasso.get()
                    .load(GlobalClass.posturl+postsData.get(position).getPostPic())
                    .resize(width, height * 2 / 5)
                    .into(holder.postPic);
        }

        String string=postsData.get(position).getPostTime();
        Date date=null;
        try {
            date=(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(string.replaceAll("Z$","+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (DateFormat.is24HourFormat(context)) {
            holder.postsTime.setText(new SimpleDateFormat("MMM dd yyyy hh:mm:ss").format(date));
        } else {
            holder.postsTime.setText(new SimpleDateFormat("MMM dd yyyy hh:mm:ss a").format(date));
        }
    }
}
