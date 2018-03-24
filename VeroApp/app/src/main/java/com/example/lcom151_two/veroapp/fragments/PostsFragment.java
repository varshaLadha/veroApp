package com.example.lcom151_two.veroapp.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.ModalClasses.PostsModelClass;
import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.adapters.PostsDisplayAdapter1;
import com.example.lcom151_two.veroapp.apiClasses.AddPostResponse;
import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.Message;
import com.example.lcom151_two.veroapp.apiClasses.getPostsResponseModel;

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

    RecyclerView posts;
    SharedPreferences sp;
    ApiInterface apiInterface;
    PostsDisplayAdapter1 adapter;
    FloatingActionButton fab;
    String mediapath,userId;
    View view1;
    PopupWindow popupWindow;
    int p;
    Bitmap bitmap;
    ImageView postpic;
    android.support.design.widget.CoordinatorLayout coordinatorLayout;
    int PICK_IMAGE_REQUEST = 111;
    PostsModelClass postData;
    ArrayList<PostsModelClass> postsDataList;

    public static PostsFragment newInstance() {

        Bundle args = new Bundle();

        PostsFragment fragment = new PostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PostsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_posts,container,false);

        initViews(view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });

        getPosts();

        return view;
    }

    public void initViews(View view){
        sp=getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        posts=(RecyclerView) view.findViewById(R.id.postsGrid);
        coordinatorLayout=view.findViewById(R.id.coordinatelayout);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        apiInterface= ApiClient.getClient().create(ApiInterface.class);
        userId=sp.getString("userId","");
        postsDataList=new ArrayList<PostsModelClass>();
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

    public void getPosts(){

        Call<getPostsResponseModel> call = apiInterface.getPosts(userId);
        call.enqueue(new Callback<getPostsResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<getPostsResponseModel> call, Response<getPostsResponseModel> response) {
                if(response.code()==200){
                    List<Message> data=response.body().getMessage();

                    for(int i=0;i<data.size();i++){
                        postsDataList.add(new PostsModelClass(data.get(i).getPostText(),data.get(i).getCreatedAt(),data.get(i).getPostUrl(),data.get(i).getUserProfilePhoto(),data.get(i).getDisplayName(),data.get(i).getComents(),data.get(i).getLikes(),data.get(i).getPostId()));
                    }

                    adapter=new PostsDisplayAdapter1(getContext(),postsDataList);
                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                    posts.setLayoutManager(layoutManager);
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
    }

    public void addPost(){
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
        TextView title=view1.findViewById(R.id.title);

        title.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

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
                    sendPostWithImage("text/img",postContent,userId,p);
                }
            }
        });
    }

    public void sendPost(String postType,String postText,String userId,Integer privacy){
        Call<AddPostResponse> call=apiInterface.post(postType,postText,userId,privacy);
        call.enqueue(new Callback<AddPostResponse>() {
            @Override
            public void onResponse(Call<AddPostResponse> call, Response<AddPostResponse> response) {
                if(response.code()==200){
                    Toast.makeText(getContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), response.code()+"", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Post adding failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddPostResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failure occurred : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Failure occurred",t.getMessage());
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

        Call<AddPostResponse> call=apiInterface.postWithImage(requestBodyHashMap,body);
        call.enqueue(new Callback<AddPostResponse>() {
            @Override
            public void onResponse(Call<AddPostResponse> call, Response<AddPostResponse> response) {
                if(response.code()==200){
                    Toast.makeText(getContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
                    mediapath=null;
                }else {
                    Toast.makeText(getContext(), "Post adding failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddPostResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failure occurred : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
