package com.example.lcom151_two.veroapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.GlobalClass;
import com.example.lcom151_two.veroapp.LocalDatabase.DatabaseHandler;
import com.example.lcom151_two.veroapp.LocalDatabase.FaviouratePosts;
import com.example.lcom151_two.veroapp.apiClasses.PostsModelClass;
import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.CommentResponseModel;
import com.example.lcom151_two.veroapp.apiClasses.LikedPostsid;
import com.example.lcom151_two.veroapp.apiClasses.PostsLikedResponseModel;
import com.example.lcom151_two.veroapp.apiClasses.UserCommentPost;
import com.example.lcom151_two.veroapp.apiClasses.UserLikedPost;
import com.example.lcom151_two.veroapp.apiClasses.UsernameCommentPost;
import com.example.lcom151_two.veroapp.apiClasses.UsernameLikedPost;
import com.example.lcom151_two.veroapp.apiClasses.postLikeResponseModel;
import com.example.lcom151_two.veroapp.fragments.PostsFragment;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsDisplayAdapter1 extends RecyclerView.Adapter<PostsDisplayAdapter1.MyViewHolder> {

    ArrayList<Integer> likedPostid,faviouratesPostid;
    SharedPreferences sp;
    String userId;
    int height, width;
    Context context;
    ArrayList<PostsModelClass> postsData;
    PostsModelClass postsModelClass;
    DatabaseHandler db;

    public PostsDisplayAdapter1(Context context, ArrayList<PostsModelClass> postsData) {
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

        holder.addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Post remove","Removing post from faviourate");
                holder.addToFav.setVisibility(View.GONE);
                holder.favPost.setVisibility(View.VISIBLE);
                postsModelClass=new PostsModelClass(postsData.get(position).getPostText(),postsData.get(position).getPostTime(),postsData.get(position).getPostPic(),postsData.get(position).getUserProfile(),postsData.get(position).getDisplayName(),postsData.get(position).getComments(),postsData.get(position).getLikescnt(),postsData.get(position).getPostId());
                long success=db.addPost(new FaviouratePosts(postsModelClass));
                if(success>0){
                    Toast.makeText(context, "Post added to faviourate ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Problem adding post to faviourate", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.favPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.favPost.setVisibility(View.GONE);
                holder.addToFav.setVisibility(View.VISIBLE);
                postsModelClass=new PostsModelClass(postsData.get(position).getPostText(),postsData.get(position).getPostTime(),postsData.get(position).getPostPic(),postsData.get(position).getUserProfile(),postsData.get(position).getDisplayName(),postsData.get(position).getComments(),postsData.get(position).getLikescnt(),postsData.get(position).getPostId());
                long success=db.deletePost(new FaviouratePosts(postsModelClass));
                if(success>0){
                    Toast.makeText(context, "Post removed from faviourates", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Problem removing post", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        List<FaviouratePosts> postsList=db.getAllPosts();
        for (FaviouratePosts posts : postsList){
            faviouratesPostid.add(posts.getPostId());
        }

        for (int i=0;i<faviouratesPostid.size();i++){
            if (faviouratesPostid.get(i)==postsData.get(position).getPostId()){
                holder.addToFav.setVisibility(View.GONE);
                holder.favPost.setVisibility(View.VISIBLE);
                return;
            }else {
                holder.addToFav.setVisibility(View.VISIBLE);
                holder.favPost.setVisibility(View.GONE);
            }
        }

        holder.likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost(position,holder);
            }
        });

        holder.likescnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLikes(position);
            }
        });

        holder.commentcnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsDisplay(position,holder);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsDisplay(position,holder);
            }
        });

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

    public void likePost(int position,MyViewHolder holder){
        holder.likescnt.setText((Integer.parseInt((String) holder.likescnt.getText()) + 1) + "");
        holder.likePost.setImageResource(R.drawable.ic_action_likedheart);
        holder.likePost.setClickable(false);
        retrofit2.Call<postLikeResponseModel> call=GlobalClass.apiInterface.likePost(postsData.get(position).getPostId(),userId);
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

    public void viewLikes(int position){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.usersnamelikedpost, null);
        final ListView listView = view.findViewById(R.id.displayUsername);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        Call<UserLikedPost> call1 = GlobalClass.apiInterface.username(postsData.get(position).getPostId());
        call1.enqueue(new Callback<UserLikedPost>() {
            @Override
            public void onResponse(Call<UserLikedPost> call, Response<UserLikedPost> response) {
                UserLikedPost name = response.body();
                if (name.getStatus() == 1) {
                    List<UsernameLikedPost> usernames = name.getMessage();
                    ArrayList<String> names = new ArrayList<String>();
                    for (int i = 0; i < usernames.size(); i++) {
                        names.add(usernames.get(i).getDisplayName());
                    }

                    listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, names));
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();
                }
            }

            @Override
            public void onFailure(Call<UserLikedPost> call, Throwable t) {
                Log.i("Failure occured", t.getMessage());
            }
        });
    }

    public void commentsDisplay(final int position, final MyViewHolder holder) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.commentsdisplay, null);
        final ListView listView = view.findViewById(R.id.comments);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        final Button send = (Button) view.findViewById(R.id.send);
        final EditText comment = (EditText) view.findViewById(R.id.postComment);

        Call<UserCommentPost> call1 = GlobalClass.apiInterface.comments(postsData.get(position).getPostId());
        call1.enqueue(new Callback<UserCommentPost>() {
            @Override
            public void onResponse(Call<UserCommentPost> call, Response<UserCommentPost> response) {
                UserCommentPost comments = response.body();
                if (comments.getStatus() == 1) {
                    List<UsernameCommentPost> data = comments.getMessage();
                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<String> commentText = new ArrayList<String>();
                    TextView noComments=view.findViewById(R.id.noComments);

                    if (data.size() == 0) {
                        //Toast.makeText(context, "No comments Yet", Toast.LENGTH_SHORT).show();
                        noComments.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < data.size(); i++) {
                        names.add(data.get(i).getDisplayName() + " : ");
                        commentText.add(data.get(i).getCommentText());
                    }

                    listView.setAdapter(new CommentDisplayAdapter(context, names, commentText));
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(comment.getText().toString())) {
                                Toast.makeText(context, "Please enter a comment", Toast.LENGTH_SHORT).show();
                            } else {
                                Call<CommentResponseModel> call2 = GlobalClass.apiInterface.postComment(comment.getText().toString(), userId, postsData.get(position).getPostId());
                                call2.enqueue(new Callback<CommentResponseModel>() {
                                    @Override
                                    public void onResponse(Call<CommentResponseModel> call, Response<CommentResponseModel> response) {
                                        CommentResponseModel data = response.body();
                                        if (data.getStatus() == 1) {
                                            Toast.makeText(context, "Comment added successfully", Toast.LENGTH_SHORT).show();
                                            bottomSheetDialog.dismiss();
                                            holder.commentcnt.setText((Integer.parseInt((String) holder.commentcnt.getText()) + 1) + "");
                                            new PostsFragment();
                                        } else {
                                            Toast.makeText(context, "Problem occured", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CommentResponseModel> call, Throwable t) {
                                        Toast.makeText(context, "Failure occured " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserCommentPost> call, Throwable t) {
                Toast.makeText(context, "Failure occurred " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
