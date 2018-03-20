package com.example.lcom151_two.veroapp.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.Datum;
import com.example.lcom151_two.veroapp.apiClasses.Message;
import com.example.lcom151_two.veroapp.apiClasses.getPostsResponseModel;
import com.example.lcom151_two.veroapp.postDisplayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    int p;
    Bitmap bitmap;
    String mediapath;
    ImageView postpic;
    android.support.design.widget.CoordinatorLayout coordinatorLayout;
    int PICK_IMAGE_REQUEST = 111;

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
        apiInterface= ApiClient.getClient().create(ApiInterface.class);
        final String userId=sp.getString("userId","");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater1=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view1=inflater1.inflate(R.layout.posts,null);
                mediapath=null;
                popupWindow=new PopupWindow(view1,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.showAtLocation(coordinatorLayout, Gravity.CENTER,0,0);
                Button submit=view1.findViewById(R.id.submitPost);
                final EditText postText=view1.findViewById(R.id.postText);
                final CheckBox privacy=view1.findViewById(R.id.privacy);
                Button selectPic=view1.findViewById(R.id.selectpic);
                postpic=(ImageView)view1.findViewById(R.id.postPic);

                selectPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(privacy.isChecked()){
                            p=1;
                        }else {
                            p=0;
                        }
                        String postContent=postText.getText().toString();
                        if(TextUtils.isEmpty(postContent) && mediapath==null){
                            Toast.makeText(getContext(), "Please enter data", Toast.LENGTH_SHORT).show();
                        }else if(mediapath==null){
                            sendPost("text",postContent,userId,p);
                        }else if(TextUtils.isEmpty(postContent)){
                            sendPostWithImage("image",postContent,userId,p);
                        }else {
                            sendPost("text",postContent,userId,p);
                        }
                    }
                });
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null){
            Uri filePath=data.getData();
            try{
                String[] filePathColumn={MediaStore.Images.Media.DATA};
                Cursor cursor=getActivity().getContentResolver().query(filePath,filePathColumn,null,null,null);
                assert cursor !=null;
                cursor.moveToFirst();
                int colIndex=cursor.getColumnIndex(filePathColumn[0]);
                mediapath=cursor.getString(colIndex);

                bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                postpic.setImageBitmap(bitmap);
                cursor.close();
            }catch (Exception e){
                Toast.makeText(getContext(),"Problem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Problem",e.getMessage());
            }
        }
    }

    public void sendPost(String postType,String postText,String userId,Integer privacy){
        Call<getPostsResponseModel> call=apiInterface.post(postType,postText,userId,privacy);
        call.enqueue(new Callback<getPostsResponseModel>() {
            @Override
            public void onResponse(Call<getPostsResponseModel> call, Response<getPostsResponseModel> response) {
                if(response.code()==200){
                    Toast.makeText(getContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Post adding failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<getPostsResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), "Failure occurred : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendPostWithImage(String postType1,String postText1,String userId1,Integer privacy1){
        File file=new File(mediapath);
        RequestBody requestBody=RequestBody.create(MediaType.parse("image/png"),file);
        MultipartBody.Part body=MultipartBody.Part.createFormData("userPhoto",userId1,requestBody);

        RequestBody postType=RequestBody.create(MediaType.parse("text/plain"),postType1);
        RequestBody postText =RequestBody.create(MediaType.parse("text/plain"),postText1);
        RequestBody userId =RequestBody.create(MediaType.parse("text/plain"),userId1);
        RequestBody privacy = RequestBody.create(MediaType.parse("text/plain"),privacy1+"");

        HashMap<String,RequestBody> requestBodyHashMap = new HashMap<>();
        requestBodyHashMap.put("postType",postType);
        requestBodyHashMap.put("postText",postText);
        requestBodyHashMap.put("userId",userId);
        requestBodyHashMap.put("privacy",privacy);

        Call<getPostsResponseModel> call=apiInterface.postWithImage(requestBodyHashMap,body);
        call.enqueue(new Callback<getPostsResponseModel>() {
            @Override
            public void onResponse(Call<getPostsResponseModel> call, Response<getPostsResponseModel> response) {
                if(response.code()==200){
                    Toast.makeText(getContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
                    mediapath=null;
                }else {
                    Toast.makeText(getContext(), "Post adding failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<getPostsResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), "Failure occurred : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
