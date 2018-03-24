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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lcom151_two.veroapp.R;
import com.example.lcom151_two.veroapp.UserDataModelClass;
import com.example.lcom151_two.veroapp.UserHome;
import com.example.lcom151_two.veroapp.apiClasses.ApiClient;
import com.example.lcom151_two.veroapp.apiClasses.ApiInterface;
import com.example.lcom151_two.veroapp.apiClasses.userProfileResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    SharedPreferences sp;
    String userId,email,mediapath,object;
    Button profileSelect,updateProfile;
    EditText username,displayname,status;
    ImageView userpic;
    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    ApiInterface apiInterface;
    SharedPreferences.Editor editor;
    UserDataModelClass udm;
    Gson gson;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);;

        initViews(view);

        profileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dname=displayname.getText().toString();
                String uname=username.getText().toString();
                String ustatus=status.getText().toString();
                if(TextUtils.isEmpty(uname) ||  TextUtils.isEmpty(dname)){
                    Toast.makeText(getContext(), "Please enter all the details", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i("Image",mediapath+"");
                    if(mediapath==null){
                        updateUserProfile(userId,email,dname,ustatus,uname);
                    }else {
                        updateUser(userId,email,dname,ustatus,uname);
                    }
                }
            }
        });



        return view;
    }

    public void initViews(View view){
        sp=getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        gson=new Gson();
        object=sp.getString("userDetail","");
        udm=gson.fromJson(object,UserDataModelClass.class);

        userId=udm.getUserId();
        email=udm.getEmail();

        apiInterface= ApiClient.getClient().create(ApiInterface.class);

        username=view.findViewById(R.id.username);
        displayname=view.findViewById(R.id.displayname);
        status=view.findViewById(R.id.status);
        userpic=view.findViewById(R.id.userpic);

        profileSelect=view.findViewById(R.id.selectpic);
        updateProfile=view.findViewById(R.id.updateProfile);

        username.setText(udm.getUserName());
        displayname.setText(udm.getDisplayName());
        status.setText(udm.getStatus());
        Picasso.get()
                .load("http://192.168.200.147:3005/profile/"+udm.getUserProfile())
                .into(userpic);
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
                userpic.setImageBitmap(bitmap);
                cursor.close();
            }catch (Exception e){
                Toast.makeText(getContext(),"Problem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUser(final String userId1, final String email1, final String displayName1, final String userStatus1, final String userName1){

        File file=new File(mediapath);
        editor=sp.edit();
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
                userProfileResponse response1=response.body();
                if(response.code()==200){
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getContext(),UserHome.class);
                    UserDataModelClass udm=new UserDataModelClass(userId1,userName1,userId1+".png",userStatus1,email1,displayName1);
                    Gson gson=new Gson();
                    String object=gson.toJson(udm);
                    editor.putString("userDetail", object);
                    editor.commit();
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Problem occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<userProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Failure : ","Message : "+t.getMessage()+" Localize Message:  "+t.getLocalizedMessage()+" Tostring : "+t.toString());
            }
        });
    }

    private void updateUserProfile(final String userId, final String email, final String displayName, final String userStatus, final String userName){
        editor=sp.edit();
        Call<userProfileResponse> call=apiInterface.profileWithoutImage(userId,email,displayName,userStatus,userName);
        call.enqueue(new Callback<userProfileResponse>() {
            @Override
            public void onResponse(Call<userProfileResponse> call, Response<userProfileResponse> response) {
                if(response.code()==200){
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getContext(),UserHome.class);
                    UserDataModelClass udm=new UserDataModelClass(userId,userName,userId+".png",userStatus,email,displayName);
                    Gson gson=new Gson();
                    String object=gson.toJson(udm);
                    editor.putString("userDetail", object);
                    editor.commit();
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Problem occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<userProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Failure : ","Message : "+t.getMessage()+" Localize Message:  "+t.getLocalizedMessage()+" Tostring : "+t.toString());
            }
        });
    }
}
