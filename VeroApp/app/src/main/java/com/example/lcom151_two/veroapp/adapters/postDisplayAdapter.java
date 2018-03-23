package com.example.lcom151_two.veroapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;

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

public class postDisplayAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> userName, postText, cmtcnt, lkscnt, postTime, posturl, userProfile;
    ArrayList<Integer> postId, likedPostid;
    SharedPreferences sp;
    String userId, url;
    ApiInterface apiInterface;
    int height, width;
    TextView displayName, postContent, commentcnt, likescnt, postsTime;
    ImageView likePost, comment, postPic, addToFav, favPost, userpic;

    public postDisplayAdapter(Context context, ArrayList<String> userName, ArrayList<String> postText, ArrayList<String> cmtcnt, ArrayList<String> lkscnt, ArrayList<Integer> postId, ArrayList<String> postTime, ArrayList<String> posturl, ArrayList<String> userProfile) {
        this.context = context;
        this.userName = userName;
        this.postText = postText;
        this.userProfile = userProfile;
        this.cmtcnt = cmtcnt;
        this.lkscnt = lkscnt;
        this.postId = postId;
        this.postTime = postTime;
        this.posturl = posturl;

        likedPostid = new ArrayList<Integer>();
        sp = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        userId = sp.getString("userId", "");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        url = "http://192.168.200.147:3005/profile/";
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
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            convertView = LayoutInflater.from(context).inflate(R.layout.postslayout, null);

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            height = metrics.heightPixels;
            width = metrics.widthPixels;

            initView(convertView);

            postsDisplay(position);

            return convertView;
        } catch (Exception e) {
            Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }

    public void initView(View convertView){
        displayName = convertView.findViewById(R.id.username);
        postContent = convertView.findViewById(R.id.postText);
        commentcnt = convertView.findViewById(R.id.commentcnt);
        likescnt = convertView.findViewById(R.id.likescnt);
        likePost = convertView.findViewById(R.id.like);
        postsTime = convertView.findViewById(R.id.postTime);
        postPic = convertView.findViewById(R.id.postpic);
        comment = convertView.findViewById(R.id.comment);
        userpic = convertView.findViewById(R.id.userpic);
        addToFav = convertView.findViewById(R.id.addToFaviourate);
        addToFav.setImageResource(R.drawable.ic_action_star_0);
        //addToFav.setVisibility(View.GONE);
        //addToFav.setClickable(false);
        //favPost=convertView.findViewById(R.id.faviouratePost);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postsDisplay(final int position){
        if (!TextUtils.isEmpty(userProfile.get(position))) {
            Picasso.get()
                    .load(url + userProfile.get(position))
                    .into(userpic);
        } else {
            userpic.setImageResource(R.drawable.user);
        }

        displayName.setText(userName.get(position));
        if (postText.get(position) == "") {
            postContent.setVisibility(View.GONE);
        } else {
            postContent.setText(postText.get(position));
        }

        commentcnt.setText(cmtcnt.get(position));
        likescnt.setText(lkscnt.get(position));

        if (!TextUtils.isEmpty(posturl.get(position))) {

            Picasso.get()
                    .load(posturl.get(position))
                    .resize(width, height * 2 / 5)
                    .into(postPic);
        }

        Call<PostsLikedResponseModel> call = apiInterface.postsLiked(userId);
        call.enqueue(new Callback<PostsLikedResponseModel>() {
            @Override
            public void onResponse(Call<PostsLikedResponseModel> call, Response<PostsLikedResponseModel> response) {
                if (response.body().getStatus() == 1) {
                    List<LikedPostsid> data = response.body().getData();
                    for (int i = 0; i < data.size(); i++) {
                        likedPostid.add(data.get(i).getPostId());
                    }

                    for (int i = 0; i < likedPostid.size(); i++) {
                        if (likedPostid.get(i) == postId.get(position)) {
                            likePost.setImageResource(R.drawable.ic_action_likedheart);
                            likePost.setClickable(false);
                            return;
                        } else {
                            likePost.setImageResource(R.drawable.ic_action_heart);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PostsLikedResponseModel> call, Throwable t) {

            }
        });

        String string=postTime.get(position);
        Date date=null;
        try {
            date=(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(string.replaceAll("Z$","+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (DateFormat.is24HourFormat(context)) {
            postsTime.setText(new SimpleDateFormat("MMM dd yyyy hh:mm:ss").format(date));
        } else {
            postsTime.setText(new SimpleDateFormat("MMM dd yyyy hh:mm:ss a").format(date));
        }

//        addToFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "added to faviourate", Toast.LENGTH_SHORT).show();
//                //addToFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_star_10));
//                addToFav.setClickable(false);
//                //addToFav.setEnabled(false);
//                Log.i("Post content", postId.get(position) + " " + postText.get(position) + " " + posturl.get(position) + " " + userName.get(position));
//            }
//        });

        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                addToFav.setVisibility(View.GONE);
                addToFav.setImageResource(R.drawable.ic_action_star_10);
            }
        });

        likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost(position);
            }
        });

        likescnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLikes(position);
            }
        });

        commentcnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsDisplay(position);
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsDisplay(position);
            }
        });
    }

    public void likePost(int position){
        likescnt.setText((Integer.parseInt((String) likescnt.getText()) + 1) + "");
        likePost.setImageResource(R.drawable.ic_action_likedheart);
        likePost.setClickable(false);
        Toast.makeText(context, "Liked post", Toast.LENGTH_SHORT).show();
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

    public void viewLikes(int position){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.usersnamelikedpost, null);
        final ListView listView = view.findViewById(R.id.displayUsername);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        Call<UserLikedPost> call1 = apiInterface.username(postId.get(position));
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

    public void commentsDisplay(final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.commentsdisplay, null);
        final ListView listView = view.findViewById(R.id.comments);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        final Button send = (Button) view.findViewById(R.id.send);
        final EditText comment = (EditText) view.findViewById(R.id.postComment);

        Call<UserCommentPost> call1 = apiInterface.comments(postId.get(position));
        call1.enqueue(new Callback<UserCommentPost>() {
            @Override
            public void onResponse(Call<UserCommentPost> call, Response<UserCommentPost> response) {
                UserCommentPost comments = response.body();
                if (comments.getStatus() == 1) {
                    List<UsernameCommentPost> data = comments.getMessage();
                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<String> commentText = new ArrayList<String>();

                    if (data.size() == 0) {
                        Toast.makeText(context, "No comments Yet", Toast.LENGTH_SHORT).show();
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
                                Call<CommentResponseModel> call2 = apiInterface.postComment(comment.getText().toString(), userId, postId.get(position));
                                call2.enqueue(new Callback<CommentResponseModel>() {
                                    @Override
                                    public void onResponse(Call<CommentResponseModel> call, Response<CommentResponseModel> response) {
                                        CommentResponseModel data = response.body();
                                        if (data.getStatus() == 1) {
                                            Toast.makeText(context, "Comment added successfully", Toast.LENGTH_SHORT).show();
                                            bottomSheetDialog.dismiss();
                                            //commentcnt.setText((Integer.parseInt((String) commentcnt.getText()) + 1) + "");
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
