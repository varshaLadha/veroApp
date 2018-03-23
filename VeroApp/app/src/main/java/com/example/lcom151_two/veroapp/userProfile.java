package com.example.lcom151_two.veroapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.apiClasses.userProfileResponse;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends BaseClass {

    ImageView imageView,userprofile;
    EditText name,email,username,status;
    Button setprofile,selectpic;
    int PICK_IMAGE_REQUEST = 111;
    String uuserId,userPhoto,mediapath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if(!sp.contains("userId")){
            Intent intent=new Intent(UserProfile.this,Login.class);
            startActivity(intent);
            finish();
        }
        else if(sp.contains("profileSet")){
            Intent intent=new Intent(UserProfile.this,UserHome.class);
            startActivity(intent);
            finish();
        }
        else {
            uuserId=sp.getString("userId","");

            initView();

            Bitmap bitmap=BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.background_splash));
            imageView.setImageBitmap(bitmap);

            selectpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
                }
            });

            setprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUserprofile();
                }
            });
        }
    }

    public void initView(){
        imageView=(ImageView)findViewById(R.id.background);
        userprofile=(ImageView)findViewById(R.id.userpic);
        name=(EditText)findViewById(R.id.displayname);
        email=(EditText)findViewById(R.id.email);
        setprofile=(Button)findViewById(R.id.setProfile);
        selectpic=(Button)findViewById(R.id.selectpic);
        status=(EditText)findViewById(R.id.status);
        username=(EditText)findViewById(R.id.username);
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.user);
    }

    public void setUserprofile(){
        String dname=name.getText().toString();
        String uemail=email.getText().toString();
        String uname=username.getText().toString();
        String ustatus=status.getText().toString();
        if(TextUtils.isEmpty(uname) || TextUtils.isEmpty(uemail) || TextUtils.isEmpty(uuserId) || TextUtils.isEmpty(dname)){
            Toast.makeText(UserProfile.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            Toast.makeText(UserProfile.this, uname+" "+uemail+" "+uuserId, Toast.LENGTH_SHORT).show();
        }else {
            if(Patterns.EMAIL_ADDRESS.matcher(uemail).matches()){
                if(mediapath==null){
                    registerUserWithoutProfile(uuserId,uemail,dname,ustatus,uname);
                }else {
                    registerUser(uuserId,uemail,dname,ustatus,uname);
                }
            }else {
                Toast.makeText(UserProfile.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Uri filePath=data.getData();
            try{
                String[] filePathColumn={MediaStore.Images.Media.DATA};
                Cursor cursor=getContentResolver().query(filePath,filePathColumn,null,null,null);
                assert cursor !=null;
                cursor.moveToFirst();
                int colIndex=cursor.getColumnIndex(filePathColumn[0]);
                mediapath=cursor.getString(colIndex);

                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                userprofile.setImageBitmap(bitmap);
                cursor.close();
            }catch (Exception e){
                Toast.makeText(this,"Problem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser(final String userId1, final String email1, final String displayName1, final String userStatus1, final String userName1){

        File file=new File(mediapath);
        RequestBody requestBody=RequestBody.create(MediaType.parse("image/png"),file);
        MultipartBody.Part body=MultipartBody.Part.createFormData("userPhoto",userId1,requestBody);

        RequestBody userId=RequestBody.create(MediaType.parse("text/plain"),userId1);
        RequestBody email =RequestBody.create(MediaType.parse("text/plain"),email1);
        RequestBody displayName =RequestBody.create(MediaType.parse("text/plain"),displayName1);
        RequestBody userStatus = RequestBody.create(MediaType.parse("text/plain"),userStatus1);
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"),userName1);

        HashMap<String,RequestBody> requestBodyHashMap = new HashMap<>();
        requestBodyHashMap.put("userId",userId);
        requestBodyHashMap.put("email",email);
        requestBodyHashMap.put("displayName",displayName);
        requestBodyHashMap.put("userStatus",userStatus);
        requestBodyHashMap.put("userName",userName);

        Call<userProfileResponse> call=apiInterface.setProfile(requestBodyHashMap,body);
        call.enqueue(new Callback<userProfileResponse>() {
            @Override
            public void onResponse(Call<userProfileResponse> call, Response<userProfileResponse> response) {
                editor.putString("profileSet",userId1);
                editor.commit();
                userProfileResponse response1=response.body();
                if(response.code()==200){
                    Intent intent=new Intent(UserProfile.this,UserHome.class);
                    UserDataModelClass udm=new UserDataModelClass(userId1,userName1,userId1+".png",userStatus1,email1,displayName1);
                    Gson gson=new Gson();
                    String object=gson.toJson(udm);
                    editor.putString("userDetail", object);
                    editor.commit();
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(UserProfile.this, "Problem occured", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<userProfileResponse> call, Throwable t) {
                Toast.makeText(UserProfile.this, "Failed"+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Failure : ","Message : "+t.getMessage()+" Localize Message:  "+t.getLocalizedMessage()+" Tostring : "+t.toString());
            }
        });
    }

    private void registerUserWithoutProfile(final String userId, final String email, final String displayName, final String userStatus, final String userName){
        editor=sp.edit();
        Call<userProfileResponse> call=apiInterface.profileWithoutImage(userId,email,displayName,userStatus,userName);
        call.enqueue(new Callback<userProfileResponse>() {
            @Override
            public void onResponse(Call<userProfileResponse> call, Response<userProfileResponse> response) {
                editor.putString("profileSet",userId);
                editor.commit();
                if(response.code()==200){
                    Intent intent=new Intent(UserProfile.this,UserHome.class);
                    UserDataModelClass udm=new UserDataModelClass(userId,userName,userId+".png",userStatus,email,displayName);
                    Gson gson=new Gson();
                    String object=gson.toJson(udm);
                    editor.putString("userDetail", object);
                    editor.commit();
                    startActivity(intent);
                }else {
                    Toast.makeText(UserProfile.this, "Problem occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<userProfileResponse> call, Throwable t) {
                Toast.makeText(UserProfile.this, "Failed "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Failure : ","Message : "+t.getMessage()+" Localize Message:  "+t.getLocalizedMessage()+" Tostring : "+t.toString());
            }
        });
    }
}
