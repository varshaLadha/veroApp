package com.example.lcom151_two.veroapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.apiClasses.AddPostResponse;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPost extends AppCompatActivity {

    Button submit,selectPic;
    EditText postText;
    CheckBox privacy;
    ImageView postpic;
    TextView title;
    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    String mediapath,userId;
    int p;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        sp=getSharedPreferences("mypref", Context.MODE_PRIVATE);
        userId=sp.getString("userId","");
        initViews();
    }

    public void initViews()
    {
        submit=findViewById(R.id.submitPost);
        postText=findViewById(R.id.postText);
        privacy=findViewById(R.id.privacy);
        selectPic=findViewById(R.id.selectpic);
        postpic=(ImageView)findViewById(R.id.postPic);
        title=findViewById(R.id.title);

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
                    Toast.makeText(AddPost.this, "Please enter data", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null){
            Uri filePath=data.getData();
            try{
                String[] filePathColumn={MediaStore.Images.Media.DATA};
                Cursor cursor=getContentResolver().query(filePath,filePathColumn,null,null,null);
                assert cursor !=null;
                cursor.moveToFirst();
                int colIndex=cursor.getColumnIndex(filePathColumn[0]);
                mediapath=cursor.getString(colIndex);

                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                postpic.setImageBitmap(bitmap);
                cursor.close();
            }catch (Exception e){
                Toast.makeText(AddPost.this,"Problem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Problem",e.getMessage());
            }
        }
    }

    public void sendPost(String postType,String postText,String userId,Integer privacy){
        Call<AddPostResponse> call=GlobalClass.apiInterface.post(postType,postText,userId,privacy);
        call.enqueue(new Callback<AddPostResponse>() {
            @Override
            public void onResponse(Call<AddPostResponse> call, Response<AddPostResponse> response) {
                if(response.code()==200){
                    Toast.makeText(AddPost.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AddPost.this,UserHome.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(AddPost.this, response.code()+"", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddPost.this, "Post adding failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddPostResponse> call, Throwable t) {
                Toast.makeText(AddPost.this, "Failure occurred : "+t.getMessage(), Toast.LENGTH_SHORT).show();
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

        Call<AddPostResponse> call=GlobalClass.apiInterface.postWithImage(requestBodyHashMap,body);
        call.enqueue(new Callback<AddPostResponse>() {
            @Override
            public void onResponse(Call<AddPostResponse> call, Response<AddPostResponse> response) {
                if(response.code()==200){
                    Toast.makeText(AddPost.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                    mediapath=null;
                    Intent intent=new Intent(AddPost.this,UserHome.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(AddPost.this, "Post adding failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddPostResponse> call, Throwable t) {
                Toast.makeText(AddPost.this, "Failure occurred : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AddPost.this,UserHome.class);
        startActivity(intent);
        finish();
    }
}
